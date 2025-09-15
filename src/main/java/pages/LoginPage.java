package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page object for Login Page
 * Uses By locators and WebDriver for actions
 */
public class LoginPage {

    private WebDriver driver;

    // Locators
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.cssSelector("button[type='submit']");
    private By errorMessage = By.id("flash");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    private WebElement waitForElement(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void typeText(By locator, String text) {
        for (int i = 0; i < 2; i++) {
            try {
                WebElement element = waitForElement(locator); // waits until clickable
                element.clear();
                element.sendKeys(text);

                // Verify text is actually entered
                String value = element.getAttribute("value");
                if (value != null && value.equals(text)) {
                    System.out.println("Thread " + Thread.currentThread().getId() +
                            " successfully entered: " + text);
                    return; // success
                }
            } catch (Exception e) {
                if (i == 1) throw e; // fail only after 2nd attempt
            }
        }
    }

//    public void enterUsername(String username) {
//        WebElement userInput = waitForElement(usernameField);
//        userInput.clear();
//        userInput.sendKeys(username);
//    }
//
//    public void enterPassword(String password) {
//        WebElement passInput = waitForElement(passwordField);
//        passInput.clear();
//        passInput.sendKeys(password);
//    }

    public void enterUsername(String username) {
        typeText(usernameField, username);
    }

    public void enterPassword(String password) {
        typeText(passwordField, password);
    }

    /** Click login button */
    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    /** Get error message text */
    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText().trim();
    }
}
