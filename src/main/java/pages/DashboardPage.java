package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for Dashboard / Secure area page
 */
public class DashboardPage {

    private WebDriver driver;

    // Locator to verify user is on dashboard
    private By secureHeader = By.cssSelector("div#content h2");

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    /** Verify if dashboard page is displayed */
    public boolean isDisplayed() {
        return driver.findElement(secureHeader).isDisplayed();
    }

    /** Get header text (optional) */
    public String getHeaderText() {
        return driver.findElement(secureHeader).getText().trim();
    }
}