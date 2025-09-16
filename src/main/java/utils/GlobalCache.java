package utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Thread-safe global cache for storing and retrieving shared objects.
 * - Useful for data that must persist across scenarios or threads
 * - Uses ConcurrentHashMap for thread-safety
 * - Optional logging can be added for debugging
 */
public class GlobalCache {

    // Thread-safe map to hold key-value pairs
    private static final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    // Private constructor prevents instantiation
    private GlobalCache() {}

    /** Store value in cache */
    public static void put(String key, Object value) {
        cache.put(key, value);
        // Optional: log action
        // System.out.println("GlobalCache: Added key=" + key);
    }

    /** Retrieve value from cache */
    public static Object get(String key) {
        return cache.get(key);
    }

    /** Retrieve value from cache with type casting */
    public static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(cache.get(key));
    }

    /** Check if key exists in cache */
    public static boolean contains(String key) {
        return cache.containsKey(key);
    }

    /** Remove specific key from cache */
    public static void remove(String key) {
        cache.remove(key);
        // Optional: log action
        // System.out.println("GlobalCache: Removed key=" + key);
    }

    /** Clear all entries in cache */
    public static void clearAll() {
        cache.clear();
        // Optional: log action
        // System.out.println("GlobalCache: Cleared all entries");
    }
}