package managers;

import base.DriverFactory;
import pages.DashboardPage;
import pages.LoginPage;
import org.openqa.selenium.WebDriver;

/**
 * PageObjectManager handles creation of page objects in a lazy, thread-safe way.
 * - Each TestContext instance has its own PageObjectManager
 * - Ensures each scenario/thread gets its own page objects
 * - Helps reduce memory usage by lazy initialization
 */
public class PageObjectManager {

    // Thread-safe WebDriver for this scenario
    private final WebDriver driver;

    // Page objects (lazy initialization)
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    public PageObjectManager() {
        // Fetch WebDriver instance from DriverFactory (thread-safe)
        this.driver = DriverFactory.getDriver();
    }

    /**
     * Get LoginPage instance
     * - Lazily initialized to save memory
     * - Can extend BasePage to inherit common actions/waits
     * @return LoginPage
     */
    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver); // Pass thread-specific WebDriver
        }
        return loginPage;
    }

    /**
     * Get DashboardPage instance
     * - Lazily initialized
     * @return DashboardPage
     */
    public DashboardPage getDashboardPage() {
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage(driver); // Pass thread-specific WebDriver
        }
        return dashboardPage;
    }

    /**
     * Optional cleanup method
     * - Clear page object references to free memory
     * - Useful in long-running test suites or parallel execution
     */
    public void clearPages() {
        loginPage = null;
        dashboardPage = null;
    }

    /**
     * Future Best Practice Notes:
     * - Add methods to get other pages as framework grows
     * - Consider a Map<String, BasePage> for dynamic page creation
     */
}