package ru.toporkov.util;

import java.io.IOException;
import java.util.Properties;

public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {

        } catch (IOException e) {
            throw new RuntimeException("Couldn't load properties file", e);
        }
    }

    private PropertiesUtil() {}

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
