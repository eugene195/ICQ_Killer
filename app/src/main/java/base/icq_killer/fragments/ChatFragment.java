package base.icq_killer.fragments;

import android.app.Fragment;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import base.icq_killer.MessageArrayAdapter;
import base.icq_killer.R;
import entities.Message;

public class ChatFragment extends Fragment {
    private static final String DESTINATION = "destination";
    private static final String NICKNAME = "nickname";

    private static String destName = "";
    private static String myName = "";

    private ListView listView;
    private MessageArrayAdapter adapter;
    private EditText msgBox;

    public static ChatFragment newInstance(String nick) {
        ChatFragment fragment = new ChatFragment();
        myName = nick;
        return fragment;
    }

    public ChatFragment() {
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            destName = savedInstanceState.getString(DESTINATION);
            myName = savedInstanceState.getString(NICKNAME);
        }

        setNames(destName);
        recreateAdapter();
        msgBox = (EditText) getView().findViewById(R.id.messageBox);
        msgBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Message msg = new Message();
                    msg.create(myName, destName, msgBox.getText().toString(), true);
                    addMsg(msg);
                    msgBox.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

    public void setNames(String dest) {
        TextView myView = (TextView) getView().findViewById(R.id.myName),
                destView = (TextView) getView().findViewById(R.id.destName);
        if ((myView != null) && (destView != null)) {
            myView.setText(myName);
            destView.setText(dest);
            destName = dest;
        }
    }

    public void changeChatroom (String dest) {
        if (!dest.equals(destName)) {
            recreateAdapter();
            setNames(dest);
        }
    }

    private void addMsg (Message msg) {
        adapter.add(msg);
    }

    private void recreateAdapter () {
        listView = (ListView) getView().findViewById(R.id.chatView);
        adapter = new MessageArrayAdapter(getActivity(), R.layout.listitem_chat);
        listView.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DESTINATION, destName);
        outState.putString(NICKNAME, myName);
    }
}
