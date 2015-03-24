package base.icq_killer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import entities.Sendable;

public class ConnectService extends Service {

    public static final String ACTION = "action";
    public static final String SEND_MESSAGE = "send_message";
    public static final String INIT_CONNECTION = "init_ws";

    private WSClient client;
    private final IBinder mBinder = new LocalBinder();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getStringExtra(ACTION);

        if (action.equals(INIT_CONNECTION)) {
            try {
                client = new WSClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new ConnectWs().execute("");

        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
    }

    public class LocalBinder extends Binder {
        ConnectService getService() {
            return ConnectService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public void send (Sendable object) {

    }

    private class ConnectWs extends AsyncTask<String, Void, String> {
        public ConnectWs() {}
        protected String doInBackground(String ... urls) {
            client.connect();
            return "";
        }
        protected void onPostExecute(String result) {
            Log.i("LocalService", "Connected WS");
        }
    }

    class WSClient {
        private WebSocketClientFactory factory = new WebSocketClientFactory();
        private WebSocket.Connection connection;
        private WebSocketClient client;
        private boolean successConn = false;
        private final String wsUrl = "ws://immense-bayou-7299.herokuapp.com/send";


        public WSClient() throws Exception {
            factory.start();
            client = factory.newWebSocketClient();
        }

        public boolean isSuccess () {
            return successConn;
        }

        public void sendMessage (String msg) throws IOException {
            connection.sendMessage(msg);
        }

        public void connect() {
            try {
                connection = client.open(new URI(wsUrl), new WebSocket.OnTextMessage() {
                    public void onOpen(Connection connection) {
                        successConn = true;
                        socketAction("Open");
                    }

                    public void onClose(int closeCode, String message) {
                        successConn = false;
                        socketAction("Close");
                    }

                    public void onMessage(String data) {
                        socketAction("Message");
                    }
                }).get(5, TimeUnit.SECONDS);

            } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }


    }
    private void socketAction (String type) {
        Log.i("LocalService", type);
    }

    private void socketSend (String msg) throws IOException {
        if (client.isSuccess())
            client.sendMessage(msg);
    }
}
