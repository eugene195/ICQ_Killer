package utils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import base.icq_killer.ClientActivity;
import entities.Sendable;
import utils.configuration.Configuration;

/**
 * Created by eugene on 03.04.15.
 */
public class SocketMessageHandler {
    public static void handleIn (String message, ClientActivity client) throws JSONException {
        switch (Configuration.PROTOCOL) {
            case "REST":
                JSONObject json = new JSONObject(message);
                String action = json.getString(Configuration.SocketAction.action);
                JSONObject data = (JSONObject) json.get(Configuration.SocketAction.data);
                client.handleJSON (action, data);
                break;
            case "SOAP":
//               TODO  PARSE XML FIRST
                client.handleXML (message);
                break;
        }
    }

    public static String handleOut (Sendable object) throws JSONException {
        ArrayList<BasicNameValuePair> parameters = object.getParams();
        JSONObject messageObject = new JSONObject();
        String message = "";
        messageObject.put(Configuration.SocketAction.action, Configuration.SocketAction.Message.action);
        switch (Configuration.PROTOCOL) {
            case "REST":
                JSONObject data = new JSONObject();
                for (BasicNameValuePair pair : parameters)
                    data.put(pair.getName(), pair.getValue());
                messageObject.put(Configuration.SocketAction.data, data);
                message = messageObject.toString();
                break;
            case "SOAP":
                message = messageObject.toString() + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>";
                String result = "";
                for (BasicNameValuePair pair : parameters)
                    result += "<" + pair.getName() + ">" + pair.getValue() + "</" + pair.getName() + ">";
                result += "</soap:Body></soap:Envelope>";
                message += result;
                break;
        }
        return message;
    }
}
