package hooks;

import context.TestContext;
import base.DriverFactory;
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
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationHooks {

    private static final Logger logger = LogManager.getLogger(ApplicationHooks.class);

    @Before
    public void setup() {
        logger.info("Starting browser...");

        // Load browser and driver version from config
        ConfigReader.loadProperties();
        String browser = ConfigReader.get("browser");
        String driverVersion = ConfigReader.get("driverVersion");

        // Initialize WebDriver (Thread-safe)
        DriverFactory.initDriver(browser, driverVersion);
        // 🔹 Launch env-specific base URL
        String baseUrl = ConfigReader.getEnvSpecific("baseUrl");
        DriverFactory.getDriver().get(baseUrl+"/login");
        logger.info("Navigated to: {}", baseUrl);
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Closing browser for scenario: {}", scenario.getName());

        // Capture screenshot on failure
        if (scenario.isFailed()) {
            logger.error("Scenario failed: {}", scenario.getName());
            byte[] screenshot = ((TakesScreenshot) DriverFactory.getDriver())
                    .getScreenshotAs(OutputType.BYTES);

            // Attach to Cucumber report
            scenario.attach(screenshot, "image/png", "Failure screenshot");

            // Save to file system
            saveScreenshotToFile(screenshot, scenario.getName());
        } else {
            logger.info("Scenario passed: {}", scenario.getName());
        }

        // Quit WebDriver
        DriverFactory.quitDriver();

        // Clear TestContext for current thread
        TestContext.clear();
    }

    /** Save screenshot to disk with timestamp */
    private void saveScreenshotToFile(byte[] screenshot, String scenarioName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "screenshots/"
                    + scenarioName.replaceAll("[^a-zA-Z0-9]", "_")
                    + "_" + timestamp + ".png";

            // Create directories if not exists
            Files.createDirectories(Paths.get(fileName).getParent());

            // Write screenshot bytes to file
            Files.write(Paths.get(fileName), screenshot);
        } catch (IOException e) {
            logger.error("Failed to save screenshot: {}", e.getMessage());
        }
    }
}
