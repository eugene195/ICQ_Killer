package base.icq_killer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import Protocol.BaseProto;
import Protocol.RestProto;
import entities.UserEntity;


public class MainActivity extends Activity {

    private Button buttonSignIn;
    private UserEntity user = new UserEntity();
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
        public GetUserName() {}
        protected void onPreExecute() {
            ProgressBarViewer.view(MainActivity.this, progressBarMsg);
        }
        protected String doInBackground(String ... urls) {
            String username = urls[0];

            // Todo кинуть исключение, если не создался пользователь
            try {
                user.create(username, protocol);
            } catch (Exception exc) {
                exc.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            ProgressBarViewer.hide();
//            if (user.isLoginSuccess())

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
            sendMessage();
        }
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
