package context;

import io.restassured.response.Response;
import managers.PageObjectManager;

/**
 * Thread-safe TestContext to hold scenario-specific data and PageObjectManager
 */
public class TestContext {

    // ThreadLocal ensures each thread (scenario) has its own TestContext instance
    private static final ThreadLocal<TestContext> context = ThreadLocal.withInitial(TestContext::new);

    private final PageObjectManager pageObjectManager;

    // Optional: store scenario-specific key-value data
    private final ScenarioContext scenarioContext;

    // ✅ Add API response storage
    private Response apiResponse;

    private TestContext() {
        this.pageObjectManager = new PageObjectManager();
        this.scenarioContext = new ScenarioContext();
    }

    /** Get the current thread's TestContext */
    public static TestContext get() {
        return context.get();
    }

    /** Get PageObjectManager for current thread/scenario */
    public PageObjectManager getPageObjectManager() {
        return pageObjectManager;
    }

    /** Get scenario-specific context */
    public ScenarioContext getScenarioContext() {
        return scenarioContext;
    }

    /** Clear TestContext for current thread (after scenario) */
    public static void clear() {
        context.remove();
    }

    /**
     * Inner class to store scenario-specific key-value data
     * Example: temporarily storing username, IDs, tokens per scenario
     */
    public static class ScenarioContext {
        private final java.util.Map<String, Object> data = new java.util.HashMap<>();

        public void set(String key, Object value) {
            data.put(key, value);
        }

        public Object get(String key) {
            return data.get(key);
        }

        public boolean contains(String key) {
            return data.containsKey(key);
        }

        public void clear() {
            data.clear();
        }
    }

    // ✅ API response getter/setter
    public void setApiResponse(Response response) {
        this.apiResponse = response;
    }

    public Response getApiResponse() {
        return apiResponse;
    }
}

