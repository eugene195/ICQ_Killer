package base.icq_killer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import base.icq_killer.fragments.ChatFragment;
import base.icq_killer.fragments.ClientListFragment;


public class ClientActivity extends FragmentActivity implements ClientListFragment.OnItemSelectedListener {

    public static final String MY_NAME = "my_name";
    public static final String CLIENT_LIST = "client_list";

    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;

    private int orientation = 1;

    ClientListFragment clf;
    ChatFragment ctf;
    private String nickname;
    private String [] clientArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            nickname = savedInstanceState.getString(MY_NAME);
            clientArray = (String []) savedInstanceState.getSerializable(CLIENT_LIST);
        }
        Intent intent = getIntent();
        try {
            fetchParams(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        clf = ClientListFragment.newInstance(nickname, clientArray);
        ctf = ChatFragment.newInstance(nickname);
        setContentView(R.layout.activity_client);

        orientation = getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_PORTRAIT) {
            showFragment(clf);
        }
    }

    @Override
    public void onClientSelected(String dest) {
        if (orientation == ORIENTATION_PORTRAIT)
            showFragment(ctf);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fetchParams (Intent intent) throws JSONException {
        nickname = intent.getStringExtra(MY_NAME);
        ArrayList<String> clients = (ArrayList<String>) intent.getSerializableExtra(CLIENT_LIST);
        if (clients != null) {
            clientArray = new String[clients.size()];
            clients.toArray(clientArray);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CLIENT_LIST, clientArray);
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
}
