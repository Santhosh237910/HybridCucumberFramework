package stepdefinitions;

import context.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.DashboardPage;
import pages.LoginPage;

public class LoginSteps {

    // Access page objects via TestContext → PageObjectManager
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;

    // Constructor initializes page objects for the current scenario thread
    public LoginSteps() {
        loginPage = TestContext.get().getPageObjectManager().getLoginPage();
        dashboardPage = TestContext.get().getPageObjectManager().getDashboardPage();
    }

    // Step: Enter username and password
    @When("username {string} and password {string} are entered")
    public void enterCredentials(String username, String password) {
        loginPage.enterUsername(username);   // Call PageObject method to enter username
        loginPage.enterPassword(password);   // Call PageObject method to enter password

        // Optional: Print thread ID to show parallel execution
        System.out.println("Thread " + Thread.currentThread().getId() +
                " running with Username=" + username + " Password=" + password);
    }

    // Step: Click the login button
    @And("the login button is clicked")
    public void clickLoginButton() {
        loginPage.clickLogin(); // Call PageObject method to perform login
    }

    // Step: Verify successful login by checking dashboard page
    @Then("the dashboard page should be displayed")
    public void verifyDashboard() {
        // Assertion with message for clarity
        Assert.assertTrue(dashboardPage.isDisplayed(), "Dashboard page is not displayed");
    }

    // Step: Verify login failure with error message
    @Then("an error message {string} should be displayed")
    public void verifyErrorMessage(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage(); // Fetch error message from PageObject

        // Assertion to check expected vs actual
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message: " + expectedMessage + ", but got: " + actualMessage);
    }
}