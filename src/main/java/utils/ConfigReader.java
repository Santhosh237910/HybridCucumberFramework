package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read configuration from config.properties
 * - Centralizes all environment, browser, API, DB, and other configs
 * - Supports environment-specific values (QA/DEV/STAGE/PROD)
 * - Validates required properties at startup
 */
public class ConfigReader {

    // 🔹 Holds all key-value pairs from config.properties
    private static Properties properties;

    // 🔹 Cache for env-specific properties to reduce repeated lookup
    private static final Properties envCache = new Properties();

    // Private constructor prevents instantiation
    private ConfigReader() {}

    /**
     * Load config.properties from classpath
     * - Called once before accessing any properties
     * - Throws RuntimeException if file is missing or fails to load
     */
    public static void loadProperties() {
        properties = new Properties();

        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("❌ config.properties file not found in classpath!");
            }

            // Load key-value pairs
            properties.load(input);

            // Optional: Validate required keys
            validateRequiredKeys("browser", "apiBaseUrl", "dbUrl", "dbUser", "dbPassword");

        } catch (IOException e) {
            throw new RuntimeException("❌ Failed to load config.properties: " + e.getMessage(), e);
        }
    }

    /**
     * Validate that required properties exist
     * @param keys Required keys
     */
    private static void validateRequiredKeys(String... keys) {
        for (String key : keys) {
            if (properties.getProperty(key) == null || properties.getProperty(key).isBlank()) {
                throw new RuntimeException("❌ Missing required property: " + key);
            }
        }
    }

    /**
     * Get property value by key
     * @param key Property name
     * @return Property value or null if key not found
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default
     * @param key Property name
     * @param defaultValue Default value if property is missing or blank
     * @return Property value or defaultValue
     */
    public static String get(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }

    /**
     * Get environment (QA/DEV/STAGE/PROD)
     * - Can be overridden via system property (-Denv=DEV)
     */
    public static String getEnv() {
        return System.getProperty("env", properties.getProperty("env", "QA"));
    }

    /**
     * Get environment-specific property
     * Example: key="baseUrl", env="DEV" → baseUrl.DEV
     */
    public static String getEnvSpecific(String key) {
        String env = getEnv();

        // Check cache first
        String cacheKey = key + "." + env;
        if (envCache.containsKey(cacheKey)) {
            return envCache.getProperty(cacheKey);
        }

        String value = properties.getProperty(cacheKey);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("❌ No property found for " + cacheKey);
        }

        // Store in cache for future lookups
        envCache.setProperty(cacheKey, value);
        return value;
    }
}