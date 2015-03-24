package entities;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eugene on 13.03.15.
 */
public class Message implements BaseEntity {
    private String text;
    private String from;
    private String to;
    private final String URL = "/message";

    public Sendable create(HashMap<String, Object> parameters) {
        String url ="/create";
        ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
        try {
            from = (String) parameters.get("from");
            to = (String) parameters.get("to");
            text = (String) parameters.get("text");
            paramList.add(new BasicNameValuePair("from", from));
            paramList.add(new BasicNameValuePair("to", to));
            paramList.add(new BasicNameValuePair("text", text));
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
        return new Sendable(paramList, URL + url);
    }

    public String getText () {
        return text;
    }

    public String getFrom () {
        return from;
    }
}
