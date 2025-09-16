package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for Dashboard / Secure area page
 * - Encapsulates UI elements and actions for the dashboard
 * - Inherits common waits and actions from BasePage
 */
public class DashboardPage extends BasePage {

    // Locator to verify if user is on dashboard page
    private final By secureHeader = By.cssSelector("div#content h2");

    // Constructor injects thread-specific WebDriver to BasePage
    public DashboardPage(WebDriver driver) {
        super(driver); // Pass driver to BasePage for common actions
    }

    /**
     * Verify if dashboard page is displayed
     * - Uses BasePage waitForClickable for stability
     * @return true if dashboard header is visible
     */
    public boolean isDisplayed() {
        return waitForClickable(secureHeader).isDisplayed();
    }

    /**
     * Get the dashboard header text (optional)
     * - Uses BasePage wait
     * @return header text as String
     */
    public String getHeaderText() {
        return waitForClickable(secureHeader).getText().trim();
    }

    /**
     * Future Best Practices:
     * - Add navigation methods (e.g., goToSettings())
     * - Add assertion helpers specific to Dashboard
     */
}