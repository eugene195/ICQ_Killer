package base.icq_killer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import base.icq_killer.fragments.ChatFragment;
import base.icq_killer.fragments.ClientListFragment;


public class ClientActivity extends FragmentActivity implements ClientListFragment.OnItemSelectedListener {

    public static final String MY_NAME = "my_name";
    public static final String CLIENT_LIST = "client_list";

    private String nickname;
    private String [] clientArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
//            clientArray = getSupportFragmentManager().getFragment(
//                    savedInstanceState, "mContent");
        }

        Intent intent = getIntent();
        try {
            fetchParams(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ClientListFragment clf = ClientListFragment.newInstance(nickname, clientArray);
        ChatFragment ctf = ChatFragment.newInstance(nickname);
        setContentView(R.layout.activity_client);
    }

    @Override
    public void onClientSelected(String dest) {
        ChatFragment fragment = (ChatFragment) getFragmentManager()
                .findFragmentById(R.id.chatFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.changeChatroom(dest);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    ChatActivity.class);
            intent.putExtra("destName", dest);
            intent.putExtra("myName", nickname);
            startActivity(intent);
        }
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
        getFragmentManager().putFragment(outState, "ChatFragment", fragment);

        ClientListFragment clientListFragment = (ClientListFragment) getFragmentManager()
                .findFragmentById(R.id.clientFragment);
        getFragmentManager().putFragment(outState, "ClientListFragment", clientListFragment);
    }
}
