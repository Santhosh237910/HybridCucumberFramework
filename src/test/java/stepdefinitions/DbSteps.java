package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import base.DBUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static base.DBUtils.getConnection;

/**
 * Step definitions for Database validation
 * - Uses DBUtils for DB connection and query execution
 * - Stores results in TestContext for thread-safe parallel execution
 * - Closes statements automatically to prevent leaks
 */
public class DbSteps {

    // Thread-safe scenario context
    private final TestContext testContext;

    public DbSteps() {
        this.testContext = TestContext.get();
    }

    /**
     * Execute SQL query and store ResultSet in scenario context
     * @param query SQL query from feature file
     */
    @When("the query {string} is executed")
    public void executeQuery(String query) {
        try {
            Statement[] stmtHolder = new Statement[1]; // array to hold Statement
            ResultSet resultSet = DBUtils.executeQuery(query, stmtHolder);

            testContext.getScenarioContext().set("dbResultSet", resultSet);
            testContext.getScenarioContext().set("dbStatement", stmtHolder[0]);

        } catch (SQLException e) {
            throw new RuntimeException("DB query execution failed: " + e.getMessage(), e);
        }
    }



    /**
     * Verify if a column contains expected value
     * @param columnName Column name to check
     * @param expectedValue Expected value
     */
    @Then("the result contains {string} with value {string}")
    public void verifyDbResult(String columnName, String expectedValue) {
        try {
            ResultSet resultSet = (ResultSet) testContext.getScenarioContext().get("dbResultSet");
            if (resultSet == null) {
                throw new RuntimeException("No DB result set found. Did you execute the query?");
            }

            boolean found = false;
            resultSet.beforeFirst(); // Reset cursor to start
            while (resultSet.next()) {
                String value = resultSet.getString(columnName);
                if (expectedValue.equals(value)) {
                    found = true;
                    break;
                }
            }

            // Assert expected value exists in DB result
            Assert.assertTrue(
                    found,
                    "Expected value '" + expectedValue + "' not found in column '" + columnName + "'"
            );

        } catch (SQLException e) {
            throw new RuntimeException("Failed to read DB result: " + e.getMessage(), e);
        } finally {
            // Close the Statement to prevent resource leaks
            Statement stmt = (Statement) testContext.getScenarioContext().get("dbStatement");
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}