package utils.configuration;

import android.content.res.Resources;

/**
 * Created by eugene on 27.03.15.
 */
public class ConfigurationManager {

    public static void configure (Resources resources) {
        ConfigLoader config = ConfigLoader.getInstance(resources);
        String protocol = config.getString("protocol");

        if (protocol.equals("SOAP")) {
            configureBySOAP();
        }
        if (protocol.equals("REST")) {
            configureByREST();
        }
        configureBySOAP();
    }

    private static void configureByREST() {

//        Everything is configured
    }

    private static void configureBySOAP() {

    }
}
