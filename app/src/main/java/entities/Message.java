package entities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eugene on 13.03.15.
 */
public class Message implements BaseEntity {
    public String text;
    public String from;
    public String to;
    public boolean isMine = false;
//    TODO didn't use it still
    private Map<String, Serializable> attachment = new HashMap<>();

    public boolean create(String from, String to, String message) {
//        TODO test realization
        text = message;
        this.from = from;
        this.to = to;
        return true;
    }

    public boolean create(String from, String to, String message, boolean mine) {
//        TODO test realization
        text = message;
        this.from = from;
        this.to = to;
        isMine = mine;
        return true;
    }

    public boolean isMsgMine () {
        return isMine;
    }
}
