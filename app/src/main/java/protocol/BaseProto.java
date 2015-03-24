package protocol;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import entities.BaseEntity;
import entities.Sendable;

/**
 * Created by eugene on 11.03.15.
 */
public interface BaseProto {

    public JSONObject create(Sendable object);


    public BaseEntity read();


    public ArrayList<BaseEntity> list();


    public void update();


    public void delete();

}
