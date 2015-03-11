package base.icq_killer;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import Protocol.BaseProto;
import Protocol.RestProto;
import entities.UserEntity;


public class MainActivity extends Activity {

    private Button buttonSignIn;
    private UserEntity user = new UserEntity();
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
        public GetUserName() {}
        protected void onPreExecute() {
            ProgressBarViewer.view(MainActivity.this, progressBarMsg);
        }
        protected String doInBackground(String ... urls) {
            String username = urls[0];
            user.create(username, protocol);
            return "";
        }
        protected void onPostExecute(String result) {
            ProgressBarViewer.hide();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
