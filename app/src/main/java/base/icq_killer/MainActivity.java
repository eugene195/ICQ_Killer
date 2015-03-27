package base.icq_killer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import entities.User;
import protocol.BaseProto;
import protocol.RestProto;
import utils.configuration.Configuration;
import utils.configuration.ConfigurationManager;
import utils.ProgressBarViewer;


public class MainActivity extends ActionBarActivity {
    private User user = new User();
    private BaseProto protocol = new RestProto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonSignIn = (Button) findViewById(R.id.loginButton);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameField = (EditText) findViewById(R.id.loginField);
                new GetUserName().execute(nameField.getText().toString());
            }
        });

        ConfigurationManager.configure(getResources());
        Configuration.logger.log("MainActivity created");
    }

    private class GetUserName extends AsyncTask<String, Void, String> {
        private final String nameCheckMsg = getResources().getString(R.string.name_check_progressbar);
        private final String userExistMsg = getResources().getString(R.string.user_exists_msg);
        private boolean success = false;
        public GetUserName() {}
        protected void onPreExecute() {
            ProgressBarViewer.view(MainActivity.this, nameCheckMsg);
        }
        protected String doInBackground(String ... urls) {
            String username = urls[0];
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put(Configuration.Login.ClientToServer.nickname, username);
            try {
                JSONObject json = protocol.create(user.create(parameters));
                if (json.getString(Configuration.Login.ServerToClient.status).
                        equals(Configuration.Login.ServerToClient.OK))
                {
                    user.setClients(json.getJSONArray(Configuration.Login.ServerToClient.clients));
                    success = true;
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
                    Configuration.logger.log("User created");
                    startActivity(clientListIntent());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            else {
                ShowMessage(userExistMsg);
            }
        }
    }

    private Intent clientListIntent () throws JSONException {
        Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
        intent.putExtra(ClientActivity.MY_NAME, user.getName());
        ArrayList<String> listdata = new ArrayList<>();
        JSONArray array = user.getClients();
        if (array != null) {
            for (int i = 0; i < user.getClients().length(); i++)
                listdata.add(array.get(i).toString());
        }
        intent.putExtra(ClientActivity.CLIENT_LIST, listdata);
        return intent;
    }

    private void ShowMessage(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_file_select, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), LogViewActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}
