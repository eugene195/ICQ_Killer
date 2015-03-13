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

    public boolean create(String name, BaseProto protocol) {
        try {
            ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
            String url ="/login";
            paramList.add(new BasicNameValuePair("nickname", name));
            JSONObject json = protocol.create(paramList, url);
//            TODO everything will fail if proto changes
            if (json.getString("status").equals("OK")) {
                clients = json.getJSONArray("clients");
                nickname = name;
                return successLogin = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
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

    public Boolean isLoginSuccess () {
        return successLogin;
    }

}
