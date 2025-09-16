package hooks;

import base.DriverFactory;
import context.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Cucumber hooks for setting up and tearing down scenarios
 * - @Before: Launch browser and navigate to base URL
 * - @After: Capture screenshots on failure, quit browser, clear TestContext
 */
public class ApplicationHooks {

    private static final Logger logger = LogManager.getLogger(ApplicationHooks.class);

    /**
     * Runs before each scenario
     * - Initializes WebDriver
     * - Loads config properties
     * - Opens base URL
     */
    @Before
    public void setup() {
        logger.info("🔹 Starting browser...");

        // Load config.properties (once per JVM, but safe to call multiple times)
        ConfigReader.loadProperties();

        // Read browser and driver version from config
        String browser = ConfigReader.get("browser");
        String driverVersion = ConfigReader.get("driverVersion");

        // Initialize WebDriver (thread-safe via ThreadLocal)
        DriverFactory.initDriver(browser, driverVersion);

        // Launch environment-specific base URL
        String baseUrl = ConfigReader.getEnvSpecific("baseUrl");
        logger.info("Navigating to base URL: {}", baseUrl);
        DriverFactory.getDriver().get(baseUrl);
    }

    /**
     * Runs after each scenario
     * - Captures screenshot if scenario fails
     * - Quits WebDriver
     * - Clears scenario-specific TestContext
     */
    @After
    public void tearDown(Scenario scenario) {
        logger.info("🔹 Ending scenario: {}", scenario.getName());

        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());

            // Capture screenshot as bytes
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", "Failure screenshot");

            // Save to disk
            saveScreenshotToFile(screenshot, scenario.getName());
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }

        // Quit driver and remove from ThreadLocal
        DriverFactory.quitDriver();

        // Clear TestContext for thread safety
        TestContext.clear();
    }

    /**
     * Save screenshot to disk
     * - Screenshots stored in target/screenshots/
     * - File name includes sanitized scenario name + timestamp
     */
    private void saveScreenshotToFile(byte[] screenshot, String scenarioName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String sanitizedScenario = scenarioName.replaceAll("[^a-zA-Z0-9]", "_");
            Path path = Path.of("target/screenshots", sanitizedScenario + "_" + timestamp + ".png");

            // Ensure directories exist
            Files.createDirectories(path.getParent());

            // Write screenshot bytes
            Files.write(path, screenshot);
            logger.info("Screenshot saved to: {}", path);
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage());
        }
    }
}