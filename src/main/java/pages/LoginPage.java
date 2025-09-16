package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Login Page
 * - Encapsulates UI elements and actions for login
 * - Inherits common waits and actions from BasePage
 * - Includes retry logic for stable text entry
 */
public class LoginPage extends BasePage {

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.id("flash");

    // Constructor injects thread-specific WebDriver to BasePage
    public LoginPage(WebDriver driver) {
        super(driver); // Pass driver to BasePage
    }

    /**
     * Type text into input fields with retry logic
     * Ensures stable input in case of transient issues
     */
    private void typeTextWithRetry(By locator, String text) {
        for (int i = 0; i < 2; i++) { // Retry twice
            try {
                WebElement element = waitForClickable(locator); // Wait from BasePage
                element.clear();
                element.sendKeys(text);

                // Verify text is entered correctly
                String value = element.getAttribute("value");
                if (value != null && value.equals(text)) {
                    System.out.println("Thread " + Thread.currentThread().getId() +
                            " successfully entered: " + text);
                    return; // Success
                }
            } catch (Exception e) {
                if (i == 1) throw e; // Fail only after 2nd attempt
            }
        }
    }

    /**
     * Enter username
     */
    public void enterUsername(String username) {
        typeTextWithRetry(usernameField, username);
    }

    /**
     * Enter password
     */
    public void enterPassword(String password) {
        typeTextWithRetry(passwordField, password);
    }

    /**
     * Click login button
     */
    public void clickLogin() {
        click(loginButton); // Use BasePage click method
    }

    /**
     * Get error message text
     * @return trimmed error message
     */
    public String getErrorMessage() {
        return getText(errorMessage); // Use BasePage getText
    }

    /**
     * Future Best Practices:
     * - Add methods for "remember me" checkbox, forgot password link
     * - Add logging wrapper instead of System.out.println
     */
}