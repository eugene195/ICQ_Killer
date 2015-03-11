package entities;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import Protocol.BaseProto;

/**
 * Created by eugene on 11.03.15.
 */
public class UserEntity {

    private final String URL = "/user";
    private String username;

    public UserEntity create(String name, BaseProto protocol) {
        ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
//TODO protocol change
//        String url = URL + "/login";
        String url ="/login";
        paramList.add(new BasicNameValuePair("nickname", name));
        JSONObject json = protocol.create(paramList, url);
        String str = json.toString();
        return null;
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

    private void parse (JSONObject json) {

    }

}
