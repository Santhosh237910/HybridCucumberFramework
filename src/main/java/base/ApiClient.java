package base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

/**
 * Thread-safe API client for sending requests using RestAssured
 * - Base URI configured from config.properties
 * - Each scenario/thread gets its own RequestSpecification
 */
public class ApiClient {

    // ThreadLocal to maintain scenario-specific RequestSpecification
    private static final ThreadLocal<RequestSpecification> requestSpecHolder = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private ApiClient() {}

    /**
     * Get thread-safe RequestSpecification
     * @return RequestSpecification for the current thread/scenario
     */
    public static RequestSpecification getRequestSpec() {
        if (requestSpecHolder.get() == null) {
            String baseUrl = ConfigReader.get("apiBaseUrl");

            RequestSpecification spec = RestAssured
                    .given()
                    .baseUri(baseUrl)
                    .header("Content-Type", "application/json");

            requestSpecHolder.set(spec);
        }
        return requestSpecHolder.get();
    }

    /**
     * Clear thread-local RequestSpecification (after scenario)
     */
    public static void clear() {
        requestSpecHolder.remove();
    }
}