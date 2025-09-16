package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;

/**
 * Thread-safe WebDriver factory
 * - Uses ThreadLocal to maintain a separate driver instance per scenario/thread
 * - Supports Chrome, Firefox, Edge
 * - Handles headless mode, driver version via config, implicit waits, page load timeout
 */
public class DriverFactory {

    // 🔹 ThreadLocal ensures each thread/scenario gets its own WebDriver
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // 🔹 Private constructor prevents instantiation
    private DriverFactory() {}

    /**
     * Initialize WebDriver instance for the current thread
     *
     * @param browser       Browser type (chrome, firefox, edge)
     * @param driverVersion Specific driver version to use (latest if null/empty)
     * @return Thread-specific WebDriver
     */
    public static WebDriver initDriver(String browser, String driverVersion) {

        // Validate browser name
        if (browser == null || browser.isBlank()) {
            throw new IllegalArgumentException("Browser name cannot be null or empty");
        }

        // 🔹 Headless mode flag from config.properties (default = true)
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless", "true"));

        // Only create driver if not already initialized for this thread
        if (driver.get() == null) {
            WebDriver webdriver;

            switch (browser.toLowerCase()) {

                case "chrome" -> {
                    ChromeOptions options = new ChromeOptions();
                    if (headless) options.addArguments("--headless=new");

                    if (driverVersion != null && !driverVersion.isEmpty()) {
                        WebDriverManager.chromedriver().driverVersion(driverVersion).setup();
                    } else {
                        WebDriverManager.chromedriver().setup(); // Latest version
                    }

                    webdriver = new ChromeDriver(options);
                }

                case "firefox" -> {
                    FirefoxOptions options = new FirefoxOptions();
                    if (headless) options.addArguments("--headless=new");

                    if (driverVersion != null && !driverVersion.isEmpty()) {
                        WebDriverManager.firefoxdriver().driverVersion(driverVersion).setup();
                    } else {
                        WebDriverManager.firefoxdriver().setup();
                    }

                    webdriver = new FirefoxDriver(options);
                }

                case "edge" -> {
                    EdgeOptions options = new EdgeOptions();
                    if (headless) options.addArguments("--headless=new");

                    if (driverVersion != null && !driverVersion.isEmpty()) {
                        WebDriverManager.edgedriver().driverVersion(driverVersion).setup();
                    } else {
                        WebDriverManager.edgedriver().setup();
                    }

                    webdriver = new EdgeDriver(options);
                }

                default -> throw new IllegalArgumentException("Browser not supported: " + browser);
            }

            // Store driver in ThreadLocal for thread safety
            driver.set(webdriver);

            // Maximize browser window
            webdriver.manage().window().maximize();

            // Set implicit wait from config (default = 10 seconds)
            long implicitWait = Long.parseLong(ConfigReader.get("implicitWait", "10"));
            webdriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

            // Set page load timeout (default = 30 seconds)
            long pageLoadTimeout = Long.parseLong(ConfigReader.get("pageLoadTimeout", "30"));
            webdriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        }

        return driver.get();
    }

    /**
     * Getter for current thread's WebDriver
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Quit driver and remove reference from ThreadLocal
     * - Prevents memory leaks in parallel execution
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();  // Close browser
            driver.remove();      // Remove instance from ThreadLocal
        }
    }
}