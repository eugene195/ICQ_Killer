package base.icq_killer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import base.icq_killer.fragments.ChatFragment;


public class ChatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        setContentView(R.layout.activity_chat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nickname = extras.getString("nickname");
            ChatFragment chatFragment = (ChatFragment) getFragmentManager()
                    .findFragmentById(R.id.chatFragment);
            chatFragment.setText(nickname);
        }
    }


}
