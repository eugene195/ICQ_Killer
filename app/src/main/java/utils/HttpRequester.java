package utils;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import entities.Sendable;

/**
 * Created by eugene on 11.03.15.
 */
public class HttpRequester {

    private static final HttpClient httpclient = new DefaultHttpClient();

    public static String streamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private static HttpPost createPost (ArrayList<BasicNameValuePair> params, String url) {
        HttpPost httppost = new HttpPost(url);
        Log.i("HttpPost request", url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httppost;
    }

    public static JSONObject send (Sendable object) {
        HttpPost httppost = createPost(object.getParams(), object.getUrl());
        HttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HttpRequester.responseToObject(response);
    }

    public static JSONArray responseToArray (HttpResponse response) {
        JSONArray finalResult = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder builder = new StringBuilder();
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }
            JSONTokener tokener = new JSONTokener(builder.toString());
            finalResult = new JSONArray(tokener);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return finalResult;
    }

    public static JSONObject responseToObject (HttpResponse response) {
        BufferedReader reader;
        JSONObject finalResult = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();
            JSONTokener tokener = new JSONTokener(json);
            finalResult = new JSONObject(tokener);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return finalResult;
    }

}
