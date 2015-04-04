package base.icq_killer;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import base.icq_killer.fragments.ChatFragment;
import base.icq_killer.fragments.ClientListFragment;
import entities.Message;
import entities.Sendable;
import utils.ArrayHandler;
import utils.SocketMessageHandler;
import utils.configuration.Configuration;


public class ClientActivity extends FragmentActivity implements ClientListFragment.OnItemSelectedListener {

    public static final String MY_NAME = "my_name";
    public static final String CLIENT_LIST = "client_list";

    public static final String EVENT = "event";
    public static final String EVENT_OPEN = "open";
    public static final String EVENT_MSG = "message";
    public static final String EVENT_CLOSE = "close";
    public static final String INFO = "info";

    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    private int orientation = ORIENTATION_PORTRAIT;

    private final int AGREE_REQUEST = 1;

    private ClientListFragment clf;
    private ChatFragment ctf;
    private String myName;
    private String [] clientArray;
    private byte [] simKey;

    private ConnectService mBoundService;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            myName = savedInstanceState.getString(MY_NAME);
        Intent intent = getIntent();
        fetchParams(intent);

        clf = ClientListFragment.newInstance(myName, clientArray);
        ctf = ChatFragment.newInstance(myName);
        setContentView(R.layout.activity_client);

        orientation = getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_PORTRAIT) {
            showFragment(clf);
        }

        serviceConnect();
    }

    private void serviceConnect () {
        Intent intent = new Intent(this, ConnectService.class);
        intent.putExtra(ConnectService.NICKNAME, myName);
        startService(intent);
        doBindService();

        IntentFilter intentFilter = new IntentFilter(ConnectService.SERVICE_TYPE);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String info = intent.getStringExtra(INFO);
                String event = intent.getStringExtra(EVENT);
                Log.i(ConnectService.SERVICE_TYPE, event + " = " + info);

                switch (event) {
                    case EVENT_OPEN:
                        break;
                    case EVENT_MSG:
                        messageReceived(info);
                        break;
                    case EVENT_CLOSE:
                        showMessage("Sorry, Your socket was closed");
                        break;
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    public void showMessage(String message) {
        Toast.makeText(ClientActivity.this, message, Toast.LENGTH_LONG).show();
    }

    private void messageReceived (String message) {
        try {
            SocketMessageHandler.handleIn(message, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startDownload (String url) {
        Intent intent = new Intent(getApplicationContext(), DownloadDialog.class).putExtra("url", url);
        startActivityForResult(intent, AGREE_REQUEST);
    }


    public void send (Sendable object) {
        try {
            mBoundService.send(SocketMessageHandler.handleOut(object));
        }
        catch (JSONException | IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void onClientSelected(String dest) {
        ctf.setNames(myName, dest);
        if (orientation == ORIENTATION_PORTRAIT)
            showFragment(ctf);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MY_NAME, myName);

        ChatFragment fragment = (ChatFragment) getFragmentManager()
                .findFragmentById(R.id.chatFragment);
        if (fragment != null)
            getFragmentManager().putFragment(outState, ChatFragment.name, fragment);

        ClientListFragment clientListFragment = (ClientListFragment) getFragmentManager()
                .findFragmentById(R.id.clientFragment);
        if (clientListFragment != null)
            getFragmentManager().putFragment(outState, ClientListFragment.name, clientListFragment);
    }

    @Override
    public void onBackPressed()
    {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void fetchParams (Intent intent) {
        myName = intent.getStringExtra(MY_NAME);
        ArrayList<String> clients = (ArrayList<String>) intent.getSerializableExtra(CLIENT_LIST);
        if (clients != null) {
            clientArray = new String[clients.size()];
            clients.toArray(clientArray);
        }
    }


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((ConnectService.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };

    private void doBindService() {
        bindService(new Intent(ClientActivity.this,
                ConnectService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AGREE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String answer = data.getStringExtra("answer");
                if (answer.equals("Yes")) {
                    showMessage("Starting download");
                    String url = data.getStringExtra("url");
                    String servicestring = Context.DOWNLOAD_SERVICE;
                    DownloadManager downloadmanager = (DownloadManager) getSystemService(servicestring);
                    Uri uri = Uri.parse(url);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    Long reference = downloadmanager.enqueue(request);
                }
                else {
                    showMessage("You canceled this download");
                }
            }
        }
    }

    private void doUnbindService() {
        unbindService(mConnection);
    }


    public void handleJSON(String action, JSONObject data) throws JSONException {
        if (action.equals(Configuration.SocketAction.ComeIn.action)) {
            String in = data.getString(Configuration.SocketAction.ComeIn.ServerToClient.nickname);
            if ((!in.equals("")) && (!ArrayHandler.findInArray(in, clientArray)))
                clientArray = ArrayHandler.addToArray(clientArray, in);
            clf.setClients(clientArray);
            if (clientArray.length == 0)
                showMessage(getResources().getString(R.string.empty_client_list));
        }
        else if (action.equals(Configuration.SocketAction.Message.action)) {
            Message msg = new Message();
            HashMap<String, Object> msgParams = new HashMap<>();
            msgParams.put(Configuration.SocketAction.Message.ServerToClient.from, data.get(Configuration.SocketAction.Message.ServerToClient.from));
            msgParams.put(Configuration.SocketAction.Message.ServerToClient.to, myName);
            msgParams.put(Configuration.SocketAction.Message.ServerToClient.message, data.get(Configuration.SocketAction.Message.ServerToClient.message));
            msg.create(msgParams);
            if (! ctf.receiveMsg(msg))
                showMessage(getResources().getString(R.string.wrong_abonent) + " " + msg.getFrom());
        }
        else if (action.equals(Configuration.SocketAction.GoOut.action)) {
            String out = data.getString(Configuration.SocketAction.GoOut.ServerToClient.nickname);
            if (ArrayHandler.findInArray(out, clientArray))
                clientArray = ArrayHandler.removeFromArray(clientArray, out);
            clf.setClients(clientArray);
            if (clientArray.length == 0)
                showMessage("Client list is empty");
        }
        else if (action.equals(Configuration.SocketAction.Download.action)) {
            Message msg = new Message();
            HashMap<String, Object> msgParams = new HashMap<>();
            msgParams.put(Configuration.SocketAction.Download.ServerToClient.from, data.get(Configuration.SocketAction.Download.ServerToClient.from));
            msgParams.put(Configuration.SocketAction.Message.ServerToClient.to, myName);
            msgParams.put(Configuration.SocketAction.Message.ServerToClient.message, data.get(Configuration.SocketAction.Download.ServerToClient.url));
            msg.create(msgParams);
            ctf.receiveMsg(msg);
            startDownload(msg.getText());
        }
        else if (action.equals(Configuration.SocketAction.EncryptStart.action)) {
            simKey = data.get(Configuration.SocketAction.EncryptStart.ServerToClient.simKey).toString().getBytes();
        }
    }

    public void handleXML (String message) {

    }
}
