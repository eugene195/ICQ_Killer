package Protocol;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import base.icq_killer.R;
import entities.BaseEntity;


/**
 * Created by eugene on 11.03.15.
 */
public class RestProto implements BaseProto {

//    private final String serverUrl = getResources().getString(R.string.server_url);
    private final String serverUrl = "localhost";

    @Override
    public JSONObject create(HashMap<String, String> params) {
        StringBuilder builder = new StringBuilder();
        builder.append(serverUrl);
        String url = params.get("url");
        params.remove("url");
        builder.append(url).append('?');
        for (String paramKey: params.keySet()) {
            String param = params.get(paramKey);
            builder.append(paramKey).append('=').append(param).append('&');
            params.remove(paramKey);
        }
        builder.deleteCharAt(builder.length() - 1);
        return HttpRequester.getResponseByUrl(builder.toString());
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
