package automation.library.common;

import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * utility class based on Apache Commons extension to java properties file.
 * additional capabilities provided by the Apache util include nested property files
 * and multi occurrences of keys (ie. array lists)
 */
public class Property {

    /**
     * gets properties file
     */
    public static PropertiesConfiguration getProperties(String propsPath) {
        PropertiesConfiguration props = new PropertiesConfiguration();
        try {
            File propsFile = new File(propsPath);
            FileInputStream inputStream = new FileInputStream(propsFile);
            props.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            throw new NoSuchElementException("Property file "+propsPath+" could not be read");
        }
        return props;
    }

    public static Properties getPropertiesH(String propsPath) {
        Properties props = new Properties();
        try {
            File propsFile = new File(propsPath);
            FileInputStream inputStream = new FileInputStream(propsFile);
            props.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            throw new NoSuchElementException("Property file "+propsPath+" could not be read");
        }
        return props;
    }

    /**
     * gets string data from any properties file on given path
     */
    public static String getProperty(String propsPath, String key)  {

		return getProperties(propsPath).getString(key);
    }

    /**
     * gets string array data from any properties file on given path
     */
    public static String[] getPropertyArray(String propsPath, String key)  {
        return getProperties(propsPath).getStringArray(key);
    }

    /**
     * gets value for variable based on preference of system property first then environment variable
     */
    public static String getVariable(String propname) {
        String val = System.getProperty(propname, null);
        val = (val == null ? System.getenv(propname) : val);
        return val;
    }

    public static HashMap<String, String> getCookies(String propsPath) {

        HashMap<String, String> mapToReturn = new HashMap<>();

        for (Map.Entry<Object, Object> entry :  getPropertiesH(propsPath).entrySet()) {
            String runningKey = (String) entry.getKey();
            if (runningKey.startsWith("cookie_")) {
                mapToReturn.put(runningKey.replace("cookie_", ""), (String) entry.getValue());
            }
        }

        return mapToReturn;
    }

    public static String getDomainName(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}