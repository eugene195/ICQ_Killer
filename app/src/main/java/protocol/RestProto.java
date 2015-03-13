package protocol;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import base.icq_killer.R;
import entities.BaseEntity;
import utils.HttpRequester;


/**
 * Created by eugene on 11.03.15.
 */
public class RestProto implements BaseProto {

//    private final String serverUrl = getResources().getString(R.string.server_url);
    private final String serverUrl = "http://" + R.string.server_URL;
    private final HttpClient httpclient = new DefaultHttpClient();

    private HttpPost createPost (ArrayList <BasicNameValuePair> params, String url) {
        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httppost;
    }

//    TODO check status codes
    private void validateResponse (HttpResponse response) {

    }

    @Override
    public JSONObject create(ArrayList <BasicNameValuePair> params, String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(serverUrl).append(url);
        HttpPost httppost = createPost(params, builder.toString());
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HttpRequester.responseToObject(response);
    }

    @Override
    public BaseEntity read() {
        return null;
    }

    @Override
    public ArrayList<BaseEntity> list() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
