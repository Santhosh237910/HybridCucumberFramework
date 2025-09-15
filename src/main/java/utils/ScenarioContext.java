package utils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private static final ThreadLocal<Map<String, Object>> scenarioData = ThreadLocal.withInitial(HashMap::new);

    public void set(String key, Object value) {
        scenarioData.get().put(key, value);
    }

    public Object get(String key) {
        return scenarioData.get().get(key);
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(scenarioData.get().get(key));
    }

    public boolean contains(String key) {
        return scenarioData.get().containsKey(key);
    }

    public void clear() {
        scenarioData.remove();
    }
}
