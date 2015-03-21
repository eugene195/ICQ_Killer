package base.icq_killer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketClient;
import org.eclipse.jetty.websocket.WebSocketClientFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;

public class ConnectService extends Service {

    public static final String ACTION = "action";
    public static final String SEND_MESSAGE = "send_message";
    public static final String INIT_CONNECTION = "init_ws";
    private static final int MESSAGES = 100;
    private static final int THREADS = 5;

    private ExecutorService es;
    private WSClient client;
    private ArrayBlockingQueue msgQueue = (ArrayBlockingQueue) new ArrayBlockingQueue<>(MESSAGES);

    public void onCreate() {
        super.onCreate();
        es = Executors.newFixedThreadPool(THREADS);
    }

    public ConnectService() {
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
            es.execute(client);
        } else if (action.equals(SEND_MESSAGE)) {
            msgQueue.add("hello");
        }

        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class WSClient implements Runnable {
        private static final int SLEEP_TIME = 500;
        private WebSocketClientFactory factory = new WebSocketClientFactory();
        private WebSocket.Connection connection;
        private WebSocketClient client;
        private boolean successConn = false;
        private final String wsUrl = "ws://immense-bayou-7299.herokuapp.com/send";


        public WSClient() throws Exception {
            factory.start();
            client = factory.newWebSocketClient();
        }

        public void run() {
//            Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
            while ( true ) {
                try {
                    connection = client.open(new URI(wsUrl), new WebSocket.OnTextMessage() {
                        public void onOpen(Connection connection) {
                            // open notification
                        }

                        public void onClose(int closeCode, String message) {
                            // close notification
                        }

                        public void onMessage(String data) {
                            // handle incoming message
                        }
                    }).get(5, TimeUnit.SECONDS);
                    if (connection != null) {
                        successConn = true;
                        connection.sendMessage("Hello World");
                        if (!msgQueue.isEmpty()) {
                            connection.sendMessage((String) msgQueue.take());
                        }
                    } else {
                        sleep(SLEEP_TIME);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
//            sendBroadcast(intent);
                stop();
            }
        }

        void stop() {

        }
    }
}
