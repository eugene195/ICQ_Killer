package base.icq_killer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;


import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import base.icq_killer.ClientActivity;
import base.icq_killer.FileSelectActivity;
import base.icq_killer.MessageArrayAdapter;
import base.icq_killer.R;
import entities.Message;
import utils.configuration.Configuration;
import utils.FileUploader;
import utils.ProgressBarViewer;

public class ChatFragment extends Fragment {
    public static String name = "ChatFragment";

    private static final String SAVE_DEST = "dest";
    private static final String SAVE_MY = "my";
    private static final String SAVE_MSG_LST = "msglist";

    static final int PICK_FILE_REQUEST = 1;


    private static String destName = "";
    private static String myName = "";
    private ArrayList <Message> msgList = new ArrayList<>();

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
            destName = savedInstanceState.getString(SAVE_DEST);
            myName = savedInstanceState.getString(SAVE_MY);
            msgList = (ArrayList<Message>) savedInstanceState.getSerializable(SAVE_MSG_LST);
        }
        recreateAdapter();

        Button sendBtn = (Button) getView().findViewById(R.id.sendButton);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = msgBox.getText().toString();
                addMsg(text, Message.PLAINTEXT_TYPE);
                msgBox.setText("");
            }
        });

        Button uploadBtn = (Button) getView().findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FileSelectActivity.class);
                startActivityForResult(intent, PICK_FILE_REQUEST);
            }
        });

        msgBox = (EditText) getView().findViewById(R.id.messageBox);
        msgBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String text = msgBox.getText().toString();
                    addMsg(text, Message.PLAINTEXT_TYPE);
                    msgBox.setText("");
                    return false;
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

    public boolean receiveMsg (Message message) {
        if (message.getFrom().equals(destName)) {
            msgList.add(message);
            recreateAdapter();
            return true;
        }
        else
            return false;
    }



    public void setNames(String my, String dest) {
            myName = my;
            destName = dest;
        }

    private void addMsg (String text, int type) {
        if (!text.equals("")) {
            Message msg = new Message();
            HashMap<String, Object> msgParams = new HashMap<>();
            msgParams.put(Configuration.SocketAction.Message.ClientToServer.from, myName);
            msgParams.put(Configuration.SocketAction.Message.ClientToServer.to, destName);
            msgParams.put(Configuration.SocketAction.Message.ClientToServer.message, text);
            switch (type) {
                case Message.FILE_TYPE:
                    new UploadFile().execute(text);
                    break;
                case Message.PLAINTEXT_TYPE:
                    ((ClientActivity) getActivity()).send(msg.create(msgParams));
                    msgList.add(msg);
                    break;
            }

        }
    }

    private void recreateAdapter () {
        View curView = getView();
        if (curView != null) {
            listView = (ListView) curView.findViewById(R.id.chatView);
            if (listView != null) {
                adapter = new MessageArrayAdapter(getActivity(), R.layout.listitem_chat, msgList, myName);
                listView.setAdapter(adapter);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_DEST, destName);
        outState.putString(SAVE_MY, myName);
        outState.putSerializable(SAVE_MSG_LST, msgList);
    }

    @Override
    public void onDestroyView() {
        msgList.clear();
        super.onDestroyView();
    }

    private class UploadFile extends AsyncTask<String, Void, String> {
        public UploadFile() {}
        private int responseCode;
        protected void onPreExecute() {
            ProgressBarViewer.view(getActivity(), getResources().getString(R.string.file_upload_progressbar));
        }
        protected String doInBackground(String ... urls) {
            String filename = urls[0];
            responseCode = FileUploader.upload(filename, myName, destName);
            return "";
        }
        protected void onPostExecute(String result) {
            ProgressBarViewer.hide();
            if (responseCode != 200)
                ((ClientActivity) getActivity()).showMessage(getResources().getString(R.string.file_upload_error));
            else
                ((ClientActivity) getActivity()).showMessage(getResources().getString(R.string.file_upload_success));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String fileUri = data.getStringExtra("URI");
                addMsg(fileUri, Message.FILE_TYPE);
                ((ClientActivity) getActivity()).showMessage(fileUri);
            }
        }
    }

}
