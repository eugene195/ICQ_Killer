package base.icq_killer;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utils.configuration.Configuration;


public class LogViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        String selectedfile = Environment.getExternalStorageDirectory() + Configuration.LOG_FILENAME;

        TextView logs = (TextView)findViewById(R.id.log);
        try {
            FileReader fr=new FileReader(selectedfile);
            BufferedReader br=new BufferedReader(fr);
            String line = null;
            try {
                while((line = br.readLine()) != null)
                {
                    logs.append(line);
                    logs.append("\n");
                }
            } catch (IOException e) {
                ShowMessage("Wrong log format");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            ShowMessage("Log file not found");
            e.printStackTrace();
        }
    }

    private void ShowMessage(String message) {
        Toast.makeText(LogViewActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
