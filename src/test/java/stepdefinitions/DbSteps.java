package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import base.DBUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Step definitions for Database validation
 * - Uses DBUtils for DB connection and queries
 * - Each scenario uses TestContext for thread-safe execution
 */
public class DbSteps {

    // Access scenario-specific TestContext (thread-safe)
    private final TestContext testContext;

    public DbSteps() {
        this.testContext = TestContext.get();
    }

    @When("the query {string} is executed")
    public void executeQuery(String query) {
        try {
            ResultSet resultSet = DBUtils.executeQuery(query);
            // Store ResultSet in scenario context (per-thread)
            testContext.getScenarioContext().set("dbResultSet", resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("DB query failed: " + e.getMessage(), e);
        }
    }

    @Then("the result contains {string} with value {string}")
    public void verifyDbResult(String columnName, String expectedValue) {
        try {
            // Retrieve ResultSet from scenario context
            ResultSet resultSet = (ResultSet) testContext.getScenarioContext().get("dbResultSet");
            if (resultSet == null) {
                throw new RuntimeException("No DB result set found. Did you execute the query?");
            }

            boolean found = false;
            resultSet.beforeFirst(); // Reset cursor to start
            while (resultSet.next()) {
                if (resultSet.getString(columnName).equals(expectedValue)) {
                    found = true;
                    break;
                }
            }

            // Use TestNG assertion
            Assert.assertTrue(
                    found,
                    "Expected value '" + expectedValue + "' not found in column '" + columnName + "'"
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to read DB result: " + e.getMessage(), e);
        }
    }
}
