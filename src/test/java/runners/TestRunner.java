package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG runner for executing Cucumber feature files
 * - Supports parallel execution
 * - Generates HTML, JSON, Allure reports
 * - Can filter scenarios by tags
 */
@CucumberOptions(
        // Path to all feature files
        features = "src/test/resources/features",

        // Packages containing step definitions and hooks
        glue = {"stepdefinitions", "hooks"},

        // Makes console output cleaner
        monochrome = true,

        // Plugins for reporting
        plugin = {
                "pretty",                                        // Prints readable Gherkin steps in console
                "html:target/cucumber-html-reports.html",       // HTML report
                "json:target/cucumber-reports/cucumber.json",   // JSON report (useful for Allure)
                "rerun:target/rerun.txt",                       // Failed scenarios for rerun
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" // Allure report
        },

        // Run scenarios with this tag only (adjust as needed)
        tags = "@invalidLogin"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Override DataProvider to enable parallel execution of scenarios
     * - Each scenario will run in a separate thread
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}