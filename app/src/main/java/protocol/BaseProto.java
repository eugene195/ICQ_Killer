package protocol;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import entities.BaseEntity;

/**
 * Created by eugene on 11.03.15.
 */
public interface BaseProto {

    public JSONObject create(ArrayList<BasicNameValuePair> params, String url);


    public BaseEntity read();


    public ArrayList<BaseEntity> list();


    public void update();


    public void delete();

}
