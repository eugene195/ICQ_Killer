package entities;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by eugene on 24.03.15.
 */
public class Sendable {
    private String url;
    private ArrayList<BasicNameValuePair> params;

    public Sendable (ArrayList<BasicNameValuePair> parameters, String URL) {
        this.url = URL;
        this.params = parameters;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<BasicNameValuePair> getParams() {
        return params;
    }

    public void setUrl (String newUrl) {url = newUrl;}
}
