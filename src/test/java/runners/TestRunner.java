package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        // Path to feature files (relative to project root)
        features = "src/test/resources/features",

        // Package(s) containing step definitions, NOT file system path
        glue = {"stepdefinitions", "hooks"},

        monochrome = true,
        plugin = {
                "pretty",
                "html:target/cucumber-html-reports.html",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/rerun.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        tags = "@invalidLogin"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
