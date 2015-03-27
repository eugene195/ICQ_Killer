package utils.configuration;

import android.content.res.Resources;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import base.icq_killer.R;

/**
 * Created by eugene on 27.03.15.
 */

public class ConfigLoader {
    private final Map<String, String> sections = new HashMap<>();
    private static Resources resources;
    private static class LazyHolder {
        private static final ConfigLoader INSTANCE = new ConfigLoader();
    }

    public static ConfigLoader getInstance(Resources resources) {
        ConfigLoader.resources = resources;
        return LazyHolder.INSTANCE;
    }

    private ConfigLoader() {
        sections.put("protocol", resources.getString(R.string.protocol));
        sections.put("log_filename", resources.getString(R.string.log_filename));
    }

    public String getString (String key) {
        return sections.get(key);
    }

}
