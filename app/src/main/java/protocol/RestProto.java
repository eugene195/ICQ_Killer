package protocol;

import org.json.JSONObject;

import java.util.ArrayList;

import entities.BaseEntity;
import entities.Sendable;
import utils.configuration.Configuration;
import utils.HttpRequester;


/**
 * Created by eugene on 11.03.15.
 */
public class RestProto implements BaseProto {
    private final String serverUrl = Configuration.HTTP_SERVER_URL;


    @Override
    public JSONObject create(Sendable object) {
        object.setUrl(serverUrl + object.getUrl());
        return HttpRequester.send(object);
    }

    @Override
    public BaseEntity read() {
        return null;
    }

    @Override
    public ArrayList<BaseEntity> list() {
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
