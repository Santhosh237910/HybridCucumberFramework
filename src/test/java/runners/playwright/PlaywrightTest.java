package runners.playwright;

import com.microsoft.playwright.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class PlaywrightTest {
    public static void main(String[] args) {
//        String url = "https://www.saucedemo.com/";
//        String username = "standard_user";
//        String password = "secret_sauce";
//
//        WebDriver driver = initWebDriver();
//        driver.manage().window().maximize();
//        openURL(driver, url);
//        driver.findElement(By.id("user-name")).sendKeys(username);
//        driver.findElement(By.id("password")).sendKeys(password);
//        driver.findElement(By.id("login-button")).click();

        initPlaywrightWebDriver();
    }

    public static WebDriver initWebDriver() {
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver();
    }

    public static void openURL(WebDriver driver, String url) {
        driver.get(url);
    }

    public static void initPlaywrightWebDriver() {

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
                        .setChannel("chrome")
                        .setArgs(List.of("--start-maximized"))
        );

        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions()
                        .setViewportSize(null)
        ); // isolated session
        Page page = context.newPage();
        page.navigate("https://www.saucedemo.com/");
        System.out.println("Page Title: " + page.title());
        System.out.println("Page Title: " + page.url());

        page.locator("#user-name").fill("standard_user");
        page.locator("#password").fill("secret_sauce");
        page.locator("#login-button").click();
        System.out.println(getAllItemNames(page));

        browser.close(); // close browser
        playwright.close();

    }

    public static List<String> getAllItemNames(Page page) {
        return page.locator(".inventory_item_name").allInnerTexts();
    }
}
