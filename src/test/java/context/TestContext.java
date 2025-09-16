package context;

import io.restassured.response.Response;
import managers.PageObjectManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe TestContext to hold scenario-specific data and PageObjectManager
 * - Each scenario/thread has its own TestContext instance via ThreadLocal
 * - Stores PageObjectManager, ScenarioContext, and API response per scenario
 */
public class TestContext {

    // ThreadLocal ensures each thread (scenario) has its own TestContext
    private static final ThreadLocal<TestContext> context = ThreadLocal.withInitial(TestContext::new);

    private final PageObjectManager pageObjectManager; // Lazily initialized per scenario
    private final ScenarioContext scenarioContext;     // Key-value storage per scenario
    private Response apiResponse;                       // Optional API response storage

    // Private constructor to enforce ThreadLocal usage
    private TestContext() {
        this.pageObjectManager = new PageObjectManager();
        this.scenarioContext = new ScenarioContext();
    }

    /**
     * Get the TestContext for the current thread/scenario
     */
    public static TestContext get() {
        return context.get();
    }

    /**
     * Get PageObjectManager instance for current scenario
     */
    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    /**
     * Get ScenarioContext instance for current scenario
     */
    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    /**
     * Clear TestContext for current thread (typically called after scenario)
     */
    public static void clear() {
        context.remove();
    }

    /**
     * Store API response for current scenario
     */
    public void setApiResponse(Response response) {
        this.apiResponse = response;
    }

    /**
     * Retrieve API response for current scenario
     */
    public Response getApiResponse() {
        return apiResponse;
    }

    /**
     * Thread-safe ScenarioContext implementation
     */
    public static class ScenarioContext {
        private final Map<String, Object> data = new HashMap<>();

        /** Store value in scenario context */
        public void set(String key, Object value) {
            data.put(key, value);
        }

        /** Retrieve value from scenario context */
        public Object get(String key) {
            return data.get(key);
        }

        /** Type-safe retrieval */
        public <T> T get(String key, Class<T> clazz) {
            Object value = data.get(key);
            return value != null ? clazz.cast(value) : null;
        }

        /** Check if key exists */
        public boolean contains(String key) {
            return data.containsKey(key);
        }

        /** Clear scenario context */
        public void clear() {
            data.clear();
        }
    }
}