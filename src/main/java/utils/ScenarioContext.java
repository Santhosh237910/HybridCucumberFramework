package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe context for storing scenario-specific data.
 * - Each scenario/thread has its own isolated data map
 * - Useful for passing data between step definitions in the same scenario
 * - Supports type-safe retrieval and optional logging
 */
public class ScenarioContext {

    // ThreadLocal ensures each scenario/thread has its own Map
    private static final ThreadLocal<Map<String, Object>> scenarioData = ThreadLocal.withInitial(HashMap::new);

    /** Store a value in scenario context */
    public void set(String key, Object value) {
        scenarioData.get().put(key, value);
        // Optional: log action for debugging
        // System.out.println("ScenarioContext: Set key=" + key + " value=" + value);
    }

    /** Retrieve a value from scenario context */
    public Object get(String key) {
        return scenarioData.get().get(key);
    }

    /** Retrieve a value with type casting */
    public <T> T get(String key, Class<T> clazz) {
        Object value = scenarioData.get().get(key);
        if (value == null) {
            return null;
        }
        return clazz.cast(value);
    }

    /** Check if a key exists in scenario context */
    public boolean contains(String key) {
        return scenarioData.get().containsKey(key);
    }

    /** Remove a key from scenario context */
    public void remove(String key) {
        scenarioData.get().remove(key);
        // Optional: log action
        // System.out.println("ScenarioContext: Removed key=" + key);
    }

    /** Clear scenario context (typically called after scenario ends) */
    public void clear() {
        scenarioData.remove(); // Removes ThreadLocal map for current thread
        // Optional: log action
        // System.out.println("ScenarioContext: Cleared context for thread " + Thread.currentThread().getId());
    }
}