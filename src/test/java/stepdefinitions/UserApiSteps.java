package stepdefinitions;

import base.ApiClient;
import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import utils.ConfigReader;

import java.util.Map;

/**
 * Step definitions for User API testing
 * - Uses RestAssured for sending HTTP requests
 * - Stores API response in TestContext for thread-safe scenario usage
 */
public class UserApiSteps {

    private static final Logger logger = LogManager.getLogger(UserApiSteps.class);

    // Scenario-specific TestContext (thread-safe)
    private final TestContext testContext;

    // Store response for current scenario
    private Response response;

    public UserApiSteps() {
        this.testContext = TestContext.get();
    }

    /**
     * Step: Set the API base URI from configuration
     */
    @Given("the user API base URI is set")
    public void setBaseUri() {
        RestAssured.baseURI = ConfigReader.get("apiBaseUrl"); // e.g., https://api.example.com
    }

    /**
     * Step: Send a POST request with JSON body
     *
     * @param endpoint    - API endpoint (e.g., /users)
     * @param requestBody - Map containing JSON request data from feature
     */
    @When("a POST request is sent to {string} with body")
    public void sendPostRequest(String endpoint, Map<String, Object> requestBody) {
        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException("Endpoint cannot be null or blank");
        }

        logger.info("Thread {} sending POST to {} with body {}",
                Thread.currentThread().getId(), endpoint, requestBody);

        response = ApiClient.getRequestSpec()  // Thread-safe
                .body(requestBody)
                .post(endpoint);

        testContext.setApiResponse(response);

        logger.info("Response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
    }


    /**
     * Step: Verify the response status code
     *
     * @param expectedStatus - Expected HTTP status code (e.g., 200)
     */
    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatus) {
        Assert.assertEquals("Unexpected status code", expectedStatus, response.getStatusCode());
    }

    /**
     * Step: Verify a specific JSON field in the response
     *
     * @param key           - JSON key to check
     * @param expectedValue - Expected value for the key
     */
    @Then("the response contains {string} with value {string}")
    public void verifyResponseField(String key, String expectedValue) {
        String actualValue = response.jsonPath().getString(key); // Extract value from JSON
        Assert.assertEquals("Mismatch in response field " + key, expectedValue, actualValue);
    }
}