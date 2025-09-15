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

    // Access page objects via TestContext -> PageObjectManager
    private final LoginPage loginPage;
    private final DashboardPage dashboardPage;

    public LoginSteps() {
        loginPage = TestContext.get().getPageObjectManager().getLoginPage();
        dashboardPage = TestContext.get().getPageObjectManager().getDashboardPage();
    }

    @When("username {string} and password {string} are entered")
    public void enterCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        System.out.println("Thread " + Thread.currentThread().getId() +
                " running with Username=" + username + " Password=" + password);
    }

    @And("the login button is clicked")
    public void clickLoginButton() {
        loginPage.clickLogin();
    }

    @Then("the dashboard page should be displayed")
    public void verifyDashboard() {
        Assert.assertTrue(dashboardPage.isDisplayed(), "Dashboard page is not displayed");
    }

    @Then("an error message {string} should be displayed")
    public void verifyErrorMessage(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message: " + expectedMessage + ", but got: " + actualMessage);
    }
}