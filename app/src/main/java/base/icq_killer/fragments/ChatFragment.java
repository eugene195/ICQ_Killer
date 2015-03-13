package base.icq_killer.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import base.icq_killer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DESTINATION = "destination";
    private static final String NICKNAME = "nickname";

    private static String destination;
    private static String nickname;

    public static ChatFragment newInstance(String dest, String nick) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(DESTINATION, dest);
        args.putString(NICKNAME, nick);
        destination = dest;
        nickname = nick;
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            destination = getArguments().getString(DESTINATION);
            nickname = getArguments().getString(NICKNAME);
        }
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
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setText(destination);
    }

}
