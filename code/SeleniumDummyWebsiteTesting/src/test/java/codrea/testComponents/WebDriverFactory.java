package codrea.testComponents;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {
    public static WebDriver createWebDriver(String browserName){
        WebDriver driver;

        switch (browserName){
            case "firefox" -> driver = createFirefoxDriver();
            case "edge" -> driver = createEdgeDriver();
            case "chrome-headless" -> driver = createChromeHeadlessDriver();
            default -> driver = createChromeDriver();
        }

        return driver;
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        setDownloadPreferences(firefoxProfile);
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setProfile(firefoxProfile);
        return new FirefoxDriver(firefoxOptions);
    }

    private static WebDriver createEdgeDriver() {
        EdgeOptions edgeOptions = new EdgeOptions();
        setDownloadPreferences(edgeOptions);
        return new EdgeDriver(edgeOptions);
    }

    private static WebDriver createChromeHeadlessDriver() {
        ChromeOptions options = new ChromeOptions();
        setDownloadPreferences(options);
        options.addArguments("--headless", "start-maximized");
        return new ChromeDriver(options);
    }

    private static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        setDownloadPreferences(options);
        return new ChromeDriver(options);
    }

    private static void setDownloadPreferences(ChromiumOptions<?> options) {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", System.getProperty("user.dir") + "\\downloads");
        options.setExperimentalOption("prefs", prefs);
    }


    private static void setDownloadPreferences(FirefoxProfile profile) {
        //2 indicates that is a custom folder
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", System.getProperty("user.dir") + "\\downloads");
        //defines the MIME type of files that Firefox should save to disk automatically,in this case it's set to Excel files
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
}

