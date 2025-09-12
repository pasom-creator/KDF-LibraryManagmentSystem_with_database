package ru.home.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final String PATH = "src/main/resources/application.properties";
    private static final Properties PROPERTIES = new Properties();

    private PropertiesUtil() {
        throw new UnsupportedOperationException("Нельзя создать экземпляр утильного класса");
    }

    static {
        loadProperties();
    }

    public static String getProperties(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (FileInputStream file = new FileInputStream(PATH)) {
            PROPERTIES.load(file);
        } catch (IOException e) {
            System.out.println("File doesn't exist");
        }
    }
}
