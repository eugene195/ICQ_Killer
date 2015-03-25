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
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import entities.User;
import protocol.BaseProto;
import protocol.RestProto;
import utils.ProgressBarViewer;


public class MainActivity extends Activity {

    private Button buttonSignIn;
    private User user = new User();
    private BaseProto protocol = new RestProto();


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
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("nickname", username);
            try {
                JSONObject json = protocol.create(user.create(parameters));
                if (json.getString("status").equals("OK")) {
                    user.setClients(json.getJSONArray("clients"));
                }
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
}
