/**
 * ConfigPropertiesUtil
 * <p>
 * 1.0
 * <p>
 * 2022/12/29 15:32
 */

package cn.com.infosec.data.structure.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropertiesUtil {

    private static final String configFile = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try {
            InputStream resourceAsStream = ConfigPropertiesUtil.class.getClassLoader().getResourceAsStream(configFile);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getConfigProperty(String key, String defaultValue) {
        return properties.containsKey(key) ? properties.getProperty(key, defaultValue) : defaultValue;
    }

    private ConfigPropertiesUtil() {
    }
}
