package base.icq_killer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import entities.User;
import protocol.BaseProto;
import protocol.RestProto;
import utils.ProgressBarViewer;


public class MainActivity extends Activity {

    private Button buttonSignIn;
    private User user = new User();
    private BaseProto protocol = new RestProto();
    private WebSocketClient mWebSocketClient;
    private final String wsUrl = "ws://immense-bayou-7299.herokuapp.com/send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSignIn = (Button) findViewById(R.id.loginButton);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = (EditText) findViewById(R.id.loginField);
                new GetUserName().execute(nameField.getText().toString());
//                new ConnectWebSocket().execute(wsUrl);
            }
        });
    }

    private class GetUserName extends AsyncTask<String, Void, String> {
        private static final String progressBarMsg = "Searching for available name";
        private static final String errorMsg = "User already exists";
        private boolean success = false;
        public GetUserName() {}
        protected void onPreExecute() {
            ProgressBarViewer.view(MainActivity.this, progressBarMsg);
        }
        protected String doInBackground(String ... urls) {
            String username = urls[0];
            try {
                success = user.create(username, protocol);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            ProgressBarViewer.hide();
            if (success)
                try {
                    startActivity(clientListIntent());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            else {
                ShowMessage(errorMsg);
            }
        }
    }

    private class ConnectWebSocket extends AsyncTask<String, Void, String> {
        private static final String progressBarMsg = "Connecting to server";
        public ConnectWebSocket() {}
        protected void onPreExecute() {
            ProgressBarViewer.view(MainActivity.this, progressBarMsg);
        }
        protected String doInBackground(String ... urls) {
            String url = urls[0];
            connectWebSocket(url);
            return "";
        }
        protected void onPostExecute(String result) {
            ProgressBarViewer.hide();
        }
    }

    private Intent clientListIntent () throws JSONException {
        Intent intent = new Intent(getApplicationContext(),
                ClientActivity.class);
        intent.putExtra(ClientActivity.MY_NAME, user.getName());
        ArrayList<String> listdata = new ArrayList<>();
        JSONArray array = user.getClients();
        if (array != null) {
            for (int i=0; i < user.getClients().length(); i++){
                listdata.add(array.get(i).toString());
            }
        }
        intent.putExtra(ClientActivity.CLIENT_LIST, listdata);
        return intent;
    }

    private void ShowMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }


    // TODO

    private void connectWebSocket(String url) {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }
            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("WS", message);
//                        TextView textView = (TextView)findViewById(R.id.messages);
//                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }
            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }
            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
    public void sendMessage() {
//        EditText editText = (EditText)findViewById(R.id.message);
//        mWebSocketClient.send(editText.getText().toString());
        mWebSocketClient.send("Surprise");
//        editText.setText("");
    }
}
