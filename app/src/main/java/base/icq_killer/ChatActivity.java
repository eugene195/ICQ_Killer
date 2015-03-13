package base.icq_killer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import base.icq_killer.fragments.ChatFragment;
import entities.Message;


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
            String myName = extras.getString("myName");
            String destName = extras.getString("destName");
            ChatFragment chatFragment = (ChatFragment) getFragmentManager()
                    .findFragmentById(R.id.chatFragment);
            chatFragment.setNames(destName);
        }
    }
}
