package entities;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import protocol.BaseProto;

/**
 * Created by eugene on 11.03.15.
 */
public class User implements BaseEntity {

    private final String URL = "/user";
    private Boolean successLogin = false;
    private String nickname;
    private JSONArray clients = new JSONArray();

    public Sendable create(String name) {
        String url ="/login";
        ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("nickname", name));
        nickname = name;
        return new Sendable(paramList, URL + url);
    }


    public User read() {
        return null;
    }


    public ArrayList<User> list() {
        return null;
    }


    public void update() {

    }


    public void delete() {

    }

    public String getName () { return nickname; }

    public JSONArray getClients () { return clients; }

    public void setClients (JSONArray clients) { this.clients = clients; }

    public Boolean isLoginSuccess () {
        return successLogin;
    }

}
