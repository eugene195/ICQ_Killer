package base.icq_killer.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import base.icq_killer.MessageArrayAdapter;
import base.icq_killer.R;
import entities.Message;

public class ChatFragment extends Fragment {
    private static final String DESTINATION = "destination";
    private static final String NICKNAME = "nickname";

    private static String destination;
    private static String nickname;

    private ListView listView;
    private MessageArrayAdapter adapter;

    public static ChatFragment newInstance(String nick) {
        ChatFragment fragment = new ChatFragment();
//        destination = dest;
        nickname = nick;
        return fragment;
    }

    public ChatFragment() {
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

    public void setText(String item) {
        TextView view = (TextView) getView().findViewById(R.id.chatText);
        view.setText(item);

        listView = (ListView) getView().findViewById(R.id.chatView);
        adapter = new MessageArrayAdapter(getActivity(), R.layout.listitem_chat);
        listView.setAdapter(adapter);
        addItems();
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setText(destination);
    }

    private void addItems() {
//        todo
        Message msg = new Message();
        msg.create("Max", "Vasya", "Hello, Vasya");
        adapter.add(msg);
    }

}
