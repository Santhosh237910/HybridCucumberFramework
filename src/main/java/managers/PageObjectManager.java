package managers;

import base.DriverFactory;
import pages.DashboardPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;

/**
 * PageObjectManager handles creation of page objects in a lazy, thread-safe way.
 * Each TestContext instance has its own PageObjectManager.
 */
public class PageObjectManager {

    private WebDriver driver;

    // Page objects (lazy initialization)
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public PageObjectManager() {
        this.driver = DriverFactory.getDriver(); // thread-safe WebDriver
    }

    /**
     * Get LoginPage instance
     */
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
        return loginPage;
    }

    /**
     * Get DashboardPage instance
     */
    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage(driver);
        }
        return dashboardPage;
    }

    /**
     * Optional: clear references (for explicit cleanup)
     */
    public void clearPages() {
        loginPage = null;
        dashboardPage = null;
    }
}
