package utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GlobalCache {

    private static final ConcurrentMap<String, Object> cache = new ConcurrentHashMap<>();

    private GlobalCache() {
        // private constructor to prevent instantiation
    }

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static Object get(String key) {
        return cache.get(key);
    }

    public static <T> T get(String key, Class<T> clazz) {
        return clazz.cast(cache.get(key));
    }

    public static boolean contains(String key) {
        return cache.containsKey(key);
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static void clearAll() {
        cache.clear();
    }
}