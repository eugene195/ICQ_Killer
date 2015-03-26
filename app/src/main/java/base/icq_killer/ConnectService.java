package base.icq_killer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import entities.Sendable;

public class ConnectService extends Service {

    public static final String ACTION = "action";
    public static final String NICKNAME = "nickname";
    public static final String SEND_MESSAGE = "send_message";
    public static final String INIT_CONNECTION = "init_ws";

    private WSClient client;
    private String myName;
    private final IBinder mBinder = new LocalBinder();

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getStringExtra(ACTION);
        myName = intent.getStringExtra(NICKNAME);

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


    public void send (Sendable object) throws JSONException, IOException {
        if (client.isSuccess()) {
            JSONObject message = new JSONObject(), data = new JSONObject();
            ArrayList<BasicNameValuePair> parameters = object.getParams();
            for (BasicNameValuePair pair : parameters)
                data.put(pair.getName(), pair.getValue());
            message.put(ACTION, "message").put("data", data);
            send(message.toString());
        }
    }

    public void send (String message) {
        new SendWs().execute(message);
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

    private class SendWs extends AsyncTask<String, Void, String> {
        public SendWs() {}
        protected String doInBackground(String ... urls) {
            String strToSend = urls[0];
            try {
                client.sendMessage(strToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            Log.i("LocalService", "Message sent");
        }
    }

    class WSClient {
        private WebSocketClientFactory factory = new WebSocketClientFactory();
        private WebSocket.Connection connection;
        private WebSocketClient client;
        private boolean successConn = false;
        private final String wsUrl = "ws://immense-bayou-7299.herokuapp.com/message/create";


        public WSClient() throws Exception {
            factory.start();
            client = factory.newWebSocketClient();
        }

        public boolean isSuccess () {
            return successConn;
        }

        public void sendMessage (String msg) throws IOException {
            if (connection != null)
            connection.sendMessage(msg);
        }

        public void connect() {
            try {
//                connection = client.open(new URI(wsUrl + "?nickname=" + myName), new WebSocket.OnTextMessage() {
                connection = client.open(new URI(wsUrl), new WebSocket.OnTextMessage() {
                    public void onOpen(Connection connection) {
                        successConn = true;
                        socketAction("Open", "");
                    }

                    public void onClose(int closeCode, String message) {
                        successConn = false;
                        socketAction("Close", "");
                    }

                    public void onMessage(String data) {
                        socketAction("Message", data);
                    }
                }).get(5, TimeUnit.SECONDS);

            } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }


    }

    private void socketAction (String type, String message) {
        Log.i("LocalService", type);
        if (type.equals("Open")) {
            Intent i = new Intent("SocketAction").putExtra("message", "Socket is about to open").putExtra("event", "open");
            this.sendBroadcast(i);
        }
    }
}
