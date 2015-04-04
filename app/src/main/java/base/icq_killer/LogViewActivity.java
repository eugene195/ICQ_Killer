package base.icq_killer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utils.configuration.Configuration;

public class LogViewActivity extends Activity {
    static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        String log = Configuration.logger.getLog().toString();
        TextView logs = (TextView)findViewById(R.id.log);
        logs.append(log);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                String fileUri = data.getStringExtra("URI");
                TextView logs = (TextView)findViewById(R.id.log);
                try {
                    FileReader fr=new FileReader(fileUri);
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
        }
    }

    private void ShowMessage(String message) {
        Toast.makeText(LogViewActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
