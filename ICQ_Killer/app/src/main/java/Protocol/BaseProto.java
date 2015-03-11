package Protocol;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import entities.BaseEntity;

/**
 * Created by eugene on 11.03.15.
 */
public interface BaseProto {

    public JSONObject create(HashMap<String, String> params);


    public BaseEntity read();


    public ArrayList<BaseEntity> list();


    public void update();


    public void delete();

}
