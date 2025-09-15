package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.ConfigReader;

import java.util.Map;

/**
 * Step definitions for User API testing
 * - Uses RestAssured for API calls
 * - Stores response in TestContext for scenario-specific usage
 */
public class UserApiSteps {

    private final TestContext testContext;

    public UserApiSteps() {
        this.testContext = TestContext.get();
    }

    private Response response;

    @Given("the user API base URI is set")
    public void setBaseUri() {
        RestAssured.baseURI = ConfigReader.get("apiBaseUrl");
    }

    @When("a POST request is sent to {string} with body")
    public void sendPostRequest(String endpoint, Map<String, Object> requestBody) {
        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(endpoint);
        // Store in TestContext if other steps need it
        testContext.setApiResponse(response);
    }

    @Then("the response status code should be {int}")
    public void verifyStatusCode(int expectedStatus) {
        Assert.assertEquals("Unexpected status code", expectedStatus, response.getStatusCode());
    }

    @Then("the response contains {string} with value {string}")
    public void verifyResponseField(String key, String expectedValue) {
        String actualValue = response.jsonPath().getString(key);
        Assert.assertEquals("Mismatch in response field " + key, expectedValue, actualValue);
    }
}