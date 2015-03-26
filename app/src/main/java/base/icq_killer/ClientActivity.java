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
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import base.icq_killer.fragments.ChatFragment;
import base.icq_killer.fragments.ClientListFragment;
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
    private String nickname;
    private String [] clientArray;

    private ConnectService mBoundService;
    private BroadcastReceiver mReceiver;

    private boolean handshakeDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            nickname = savedInstanceState.getString(MY_NAME);
        Intent intent = getIntent();
        fetchParams(intent);

        clf = ClientListFragment.newInstance(nickname, clientArray);
        ctf = ChatFragment.newInstance(nickname);
        setContentView(R.layout.activity_client);

        orientation = getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_PORTRAIT) {
            showFragment(clf);
        }

        serviceConnect();
    }

    private void serviceConnect () {
        Intent intent = new Intent(this, ConnectService.class).putExtra(ConnectService.ACTION, ConnectService.INIT_CONNECTION);
        intent.putExtra(ConnectService.NICKNAME, nickname);
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

                if (event.equals("open"))
                    if (!handshakeDone) {
                        JSONObject handshake = new JSONObject(), data = new JSONObject();
                        try {
                            data.put("user", nickname);
                            handshake.put(ConnectService.ACTION, "handshake").put("data", data);
                            mBoundService.send(handshake.toString());
                            handshakeDone = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                else if (event.equals("message")) {
                    showMessage(message);
                }

            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    public void showMessage(String message) {
        Toast.makeText(ClientActivity.this, message, Toast.LENGTH_LONG).show();
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
        ctf.setNames(nickname, dest);
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
        outState.putString(MY_NAME, nickname);

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
        nickname = intent.getStringExtra(MY_NAME);
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
