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

import entities.BaseEntity;
import entities.Sendable;
import utils.HttpRequester;


/**
 * Created by eugene on 11.03.15.
 */
public class RestProto implements BaseProto {
    private final String serverUrl = "http://immense-bayou-7299.herokuapp.com";


    @Override
    public JSONObject create(Sendable object) {
        object.setUrl(serverUrl + object.getUrl());
        return HttpRequester.send(object);
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
