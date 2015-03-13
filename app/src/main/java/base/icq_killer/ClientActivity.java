package base.icq_killer;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import org.json.JSONException;

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
    public void onClientSelected(String nickname) {
        ChatFragment fragment = (ChatFragment) getFragmentManager()
                .findFragmentById(R.id.chatFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(nickname);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    ChatActivity.class);
            intent.putExtra("nickname", nickname);
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
}
