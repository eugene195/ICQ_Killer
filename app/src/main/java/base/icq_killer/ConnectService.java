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
import utils.configuration.Configuration;

public class ConnectService extends Service {

    public static final String SERVICE_TYPE = "SocketAction";
    public static final String NICKNAME = "nickname";

    private WSClient client;
    private String myName;

    private final IBinder mBinder = new LocalBinder();
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class LocalBinder extends Binder {
        ConnectService getService() {
            return ConnectService.this;
        }
    }

    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myName = intent.getStringExtra(NICKNAME);
        try {
            client = new WSClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ConnectWs().execute("");
        Log.i(SERVICE_TYPE, "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    public void send (String object) throws JSONException, IOException {
        if (client.isSuccess()) {
            new SendWs().execute(object);
        }
    }

    private class ConnectWs extends AsyncTask<String, Void, String> {
        public ConnectWs() {}
        protected String doInBackground(String ... urls) {
            client.connect();
            return "";
        }
        protected void onPostExecute(String result) {
            Log.i(SERVICE_TYPE, "Connected WS");
        }
    }

    private class SendWs extends AsyncTask<String, Void, String> {
        public SendWs() {}
        protected String doInBackground(String ... urls) {
            String strToSend = urls[0];
            Log.i(SERVICE_TYPE, strToSend);
            try {
                client.sendMessage(strToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            Log.i(SERVICE_TYPE, "Message sent");
        }
    }

    class WSClient {
        private WebSocketClientFactory factory = new WebSocketClientFactory();
        private WebSocket.Connection connection;
        private WebSocketClient client;
        private boolean successConn = false;
        private final String wsUrl = Configuration.WS_SERVER_URL;


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
                connection = client.open(new URI(wsUrl + "?" + Configuration.SocketAction.connparameter + "=" + myName + "&client=MOBILE"), new WebSocket.OnTextMessage() {
                    public void onOpen(Connection connection) {
                        successConn = true;
                        socketAction(ClientActivity.EVENT_OPEN, "");
                    }
                    public void onClose(int closeCode, String message) {
                        successConn = false;
                        socketAction("close", "");
                    }
                    public void onMessage(String data) {
                        socketAction(ClientActivity.EVENT_MSG, data);
                    }
                }).get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void socketAction (String type, String message) {
        Log.i(SERVICE_TYPE, type);
        Intent i = new Intent(SERVICE_TYPE);
        switch (type) {
            case ClientActivity.EVENT_OPEN:
                i.putExtra(ClientActivity.INFO, "Socket is about to open").putExtra(ClientActivity.EVENT, ClientActivity.EVENT_OPEN);
                break;
            case ClientActivity.EVENT_MSG:
                i.putExtra(ClientActivity.INFO, message).putExtra(ClientActivity.EVENT, ClientActivity.EVENT_MSG);
                break;
            case ClientActivity.EVENT_CLOSE:
                i.putExtra(ClientActivity.INFO, "Socket has been closed").putExtra(ClientActivity.EVENT, ClientActivity.EVENT_CLOSE);
                break;
        }
        this.sendBroadcast(i);
    }
}
