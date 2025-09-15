package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    // 🔹 Properties object to hold all key-value pairs from config.properties
    private static Properties properties;

    /**
     * Load config.properties from classpath
     * - Called once before accessing any properties
     * - Throws RuntimeException if file is missing or fails to load
     */
    public static void loadProperties() {
        properties = new Properties();

        // Try-with-resources ensures InputStream is closed automatically
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            // If config file is not found in classpath, throw exception
            if (input == null) {
                throw new RuntimeException("❌ config.properties file not present in classpath!");
            }

            // Load key-value pairs into Properties object
            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    /**
     * Get property value by key
     *
     * @param key Property name
     * @return Property value, or null if key not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default
     * - Returns defaultValue if key is missing or blank
     *
     * @param key          Property name
     * @param defaultValue Default value to return if key is missing
     * @return Property value or defaultValue
     */
    public static String get(String key, String defaultValue) {
        String value = properties.getProperty(key);
        // Return value if present, otherwise default
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    /** Get environment (qa/dev/stage/prod) */
    public static String getEnv() {
        return System.getProperty("env", properties.getProperty("env", "qa")).toLowerCase();
    }

    /** Get env-specific property */
    public static String getEnvSpecific(String key) {
        String env = getEnv();
        return properties.getProperty(key + "." + env);
    }
}