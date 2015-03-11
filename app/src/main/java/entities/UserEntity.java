package entities;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Protocol.BaseProto;

/**
 * Created by eugene on 11.03.15.
 */
public class UserEntity {

    private final String URL = "/user";
    private Boolean successLogin = false;
    private String nickname;
    private JSONArray clients = new JSONArray();

    public void create(String name, BaseProto protocol) {
        try {
            ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
    //TODO protocol change
    //        String url = URL + "/login";
            String url ="/login";
            paramList.add(new BasicNameValuePair("nickname", name));
            JSONObject json = protocol.create(paramList, url);
            if (json.getBoolean("result")) {
                clients = json.getJSONArray("clients");
                nickname = name;
                successLogin = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public UserEntity read() {
        return null;
    }


    public ArrayList<UserEntity> list() {
        return null;
    }


    public void update() {

    }


    public void delete() {

    }

    private Boolean isLoginSuccess () {
        return successLogin;
    }

}
