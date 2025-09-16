package base;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

/**
 * Thread-safe API client for sending requests using RestAssured
 * - Base URI is configured from config.properties
 * - Each scenario/thread gets its own RequestSpecification
 * - Supports default headers, logging, and optional auth token
 */
public class ApiClient {

    // ThreadLocal to maintain scenario-specific RequestSpecification
    private static final ThreadLocal<RequestSpecification> requestSpecHolder = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private ApiClient() {}

    /**
     * Get thread-safe RequestSpecification for current scenario/thread
     * @return RequestSpecification configured with base URI, headers, and logging
     */
    public static RequestSpecification getRequestSpec() {

        // Initialize only if not already created for this thread
        if (requestSpecHolder.get() == null) {

            // Load base URL from config.properties
            String baseUrl = ConfigReader.get("apiBaseUrl");

            // Build RestAssured RequestSpecification
            RequestSpecification spec = RestAssured
                    .given()
                    .baseUri(baseUrl)                       // Set base URI
                    .header("Content-Type", "application/json") // Set Content-Type header
                    .header("Accept", "application/json");      // Set Accept header

            // Optional: Add Authorization header if token present in config
            String token = ConfigReader.get("apiAuthToken");
            if (token != null && !token.isBlank()) {
                spec.header("Authorization", "Bearer " + token);
            }

            // Enable logging of request details for debugging/reporting
            spec.log().all();

            // Store in ThreadLocal for thread-safe usage in parallel execution
            requestSpecHolder.set(spec);
        }

        // Return thread-specific RequestSpecification
        return requestSpecHolder.get();
    }

    /**
     * Clear thread-local RequestSpecification (after scenario)
     * - Prevents memory leaks during parallel execution
     */
    public static void clear() {
        requestSpecHolder.remove(); // Remove reference for current thread
    }
}