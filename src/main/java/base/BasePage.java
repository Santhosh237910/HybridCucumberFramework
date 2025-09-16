package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage contains common reusable methods for all page objects.
 * - Waits for elements
 * - Clicks and types safely
 * - Provides a centralized location for future enhancements (e.g., logging, screenshots)
 */
public class BasePage {

    // WebDriver instance (thread-specific driver passed from DriverFactory)
    protected WebDriver driver;

    // Constructor to initialize WebDriver
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Wait until element is clickable
     * @param locator By locator of the element
     * @return WebElement once clickable
     */
    protected WebElement waitForClickable(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Click on an element safely after waiting for it
     * @param locator By locator of element to click
     */
    protected void click(By locator) {
        waitForClickable(locator).click(); // Wait + click
    }

    /**
     * Type text into input fields safely
     * - Clears field before typing
     * - Waits until element is clickable
     * @param locator By locator of input field
     * @param text Text to enter
     */
    protected void typeText(By locator, String text) {
        WebElement element = waitForClickable(locator);
        element.clear();         // Clear any existing text
        element.sendKeys(text);  // Enter new text
    }

    /**
     * Get visible text of an element
     * - Waits until element is clickable (or visible)
     * @param locator By locator of the element
     * @return Trimmed text of element
     */
    protected String getText(By locator) {
        return waitForClickable(locator).getText().trim();
    }
}