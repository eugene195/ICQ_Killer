package base.icq_killer.fragments;

import android.app.Fragment;
import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import base.icq_killer.ClientActivity;
import base.icq_killer.MainActivity;
import base.icq_killer.MessageArrayAdapter;
import base.icq_killer.R;
import entities.BaseEntity;
import entities.Message;

public class ChatFragment extends Fragment {
    private static final String DESTINATION = "destination";
    private static final String NICKNAME = "nickname";
    private static final String MESSAGE_LIST = "msglist";

    private static String destName = "";
    private static String myName = "";
    private ArrayList <Message> msgList = new ArrayList<>();

    private ListView listView;

    @Override
    public void onDestroyView() {
        msgList.clear();
        super.onDestroyView();
    }

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
            msgList = (ArrayList<Message>) savedInstanceState.getSerializable(MESSAGE_LIST);
        }

        recreateAdapter();
        msgBox = (EditText) getView().findViewById(R.id.messageBox);
        msgBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    addMsg();
                    msgBox.setText("");
                    return true;
                }
                return false;
            }
        });

        TextView myView = (TextView) getView().findViewById(R.id.myName),
                destView = (TextView) getView().findViewById(R.id.destName);
        if ((myView != null) && (destView != null)) {
            myView.setText(myName);
            destView.setText(destName);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    public void setNames(String my, String dest) {
            myName = my;
            destName = dest;
        }

    private void addMsg () {
        Message msg = new Message();
        HashMap<String, Object> msgParams = new HashMap<>();
        msgParams.put("from", myName);
        msgParams.put("to", destName);
        msgParams.put("text", msgBox.getText().toString());
        ((ClientActivity)getActivity()).send(msg.create(msgParams));
        msgList.add(msg);
    }

    private void recreateAdapter () {
        listView = (ListView) getView().findViewById(R.id.chatView);
        if (listView != null) {
            adapter = new MessageArrayAdapter(getActivity(), R.layout.listitem_chat, msgList, myName);
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DESTINATION, destName);
        outState.putString(NICKNAME, myName);
        outState.putSerializable(MESSAGE_LIST, msgList);
    }
}
