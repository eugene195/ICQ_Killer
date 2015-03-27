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
    public final String FROM = "from";
    public final String TO = "whom";
    public final String MESSAGE = "message";

    public static final int FILE_TYPE = 0;
    public static final int PLAINTEXT_TYPE = 1;

    public Sendable create(HashMap<String, Object> parameters) {
        String url ="/create";
        ArrayList <BasicNameValuePair> paramList = new ArrayList<>();
        try {
            from = (String) parameters.get(FROM);
            to = (String) parameters.get(TO);
            text = (String) parameters.get(MESSAGE);
            paramList.add(new BasicNameValuePair(FROM, from));
            paramList.add(new BasicNameValuePair(TO, to));
            paramList.add(new BasicNameValuePair(MESSAGE, text));
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
