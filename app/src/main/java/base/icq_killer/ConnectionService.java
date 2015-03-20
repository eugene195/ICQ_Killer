package base.icq_killer;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ConnectionService extends Service {

    private WebSocketClient mWebSocketClient;
    private final String wsUrl = "ws://immense-bayou-7299.herokuapp.com/send";

    public ConnectionService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }










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
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("WS", message);
////                        TextView textView = (TextView)findViewById(R.id.messages);
////                        textView.setText(textView.getText() + "\n" + message);
//                    }
//                });
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
