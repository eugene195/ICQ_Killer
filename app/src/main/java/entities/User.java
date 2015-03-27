package entities;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;

import utils.configuration.Configuration;

/**
 * Created by eugene on 11.03.15.
 */
public class User implements BaseEntity {

    public final String NICKNAME = Configuration.Login.ClientToServer.nickname;
    private Boolean successLogin = false;
    private String nickname;
    private JSONArray clients = new JSONArray();

    public Sendable create(HashMap<String, Object> parameters) {
        String name = (String) parameters.get(NICKNAME);
        ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair(NICKNAME, name));
        nickname = name;
        return new Sendable(paramList, Configuration.Login.URL);
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
