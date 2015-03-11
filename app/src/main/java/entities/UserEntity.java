package entities;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("url", URL + "/login");
        map.put("nickname", name);
        JSONObject json = protocol.create(map);
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
