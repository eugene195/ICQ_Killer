package base.icq_killer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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


public class ClientActivity extends FragmentActivity implements ClientListFragment.OnItemSelectedListener {

    public static final String MY_NAME = "my_name";
    public static final String CLIENT_LIST = "client_list";

    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    private static final String BROADCAST_ACTION = "get_message";
    private int orientation = 1;

    ClientListFragment clf;
    ChatFragment ctf;
    private String myName;
    private String [] clientArray;

    private ConnectService mBoundService;
    private BroadcastReceiver mReceiver;

    private boolean handshakeDone = false;

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
        Intent intent = new Intent(this, ConnectService.class).putExtra(ConnectService.ACTION, ConnectService.INIT_CONNECTION);
        intent.putExtra(ConnectService.NICKNAME, myName);
        startService(intent);
        doBindService();

        IntentFilter intentFilter = new IntentFilter(
                "SocketAction");
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                String event = intent.getStringExtra("event");
                Log.i("SocketAction", event + " = " + message);

                switch (event) {
                    case "open":
                        break;
                    case "message":
                        messageReceived(message);
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
            JSONObject json = new JSONObject(message);
            String action = json.getString("action");
            JSONObject data = (JSONObject) json.get("data");

            switch (action) {
                case "user_come_in":
                    String in = data.getString("nickname");
                    if ((!in.equals("")) && (!findClient(in)))
                        clientArray = addClient(clientArray, in);
                    clf.setClients(clientArray);
                    break;
                case "message":
                    Message msg = new Message();
                    HashMap<String, Object> msgParams = new HashMap<>();
                    msgParams.put("from", data.get("from"));
                    msgParams.put("whom", myName);
                    msgParams.put("message", data.get("message"));
                    msg.create(msgParams);
                    ctf.receiveMsg(msg);
                    break;
                case "user_went_out":
                    String out = data.getString("nickname");
                    if (findClient(out))
                        clientArray = removeClient(clientArray, out);
                    clf.setClients(clientArray);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean findClient (String client) {
        for (String aClientArray : clientArray)
            if (aClientArray.equals(client))
                return true;
        return false;
    }

    private String[] removeClient(String[] input, String deleteMe) {
        List <String> result = new LinkedList<>();
        for(String item : input)
            if(!deleteMe.equals(item))
                result.add(item);
        return result.toArray(input);
    }

    private String[] addClient(String[] input, String addMe) {
        List <String> result = new LinkedList<>();
        Collections.addAll(result, input);
        result.add(addMe);
        return result.toArray(input);
    }

    public void send (Sendable object) {
        try {
            mBoundService.send(object);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
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
            getFragmentManager().putFragment(outState, "ChatFragment", fragment);


        ClientListFragment clientListFragment = (ClientListFragment) getFragmentManager()
                .findFragmentById(R.id.clientFragment);
        if (clientListFragment != null)
            getFragmentManager().putFragment(outState, "ClientListFragment", clientListFragment);
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
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((ConnectService.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            mBoundService = null;
        }
    };

    void doBindService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        bindService(new Intent(ClientActivity.this,
                ConnectService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        unbindService(mConnection);
    }


}
