package codrea.testComponents;

import codrea.pageObjectModel.LoginPage;
import codrea.pageObjectModel.ProductsPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class BaseTest {
    public WebDriver driver;
    public LoginPage loginPage;

    public WebDriver initializeDriver() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/globalData.properties");
        properties.load(fis);

        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : properties.getProperty("browser");

        switch (browserName) {
            case "firefox", "edge", "chrome-headless" -> driver = WebDriverFactory.createWebDriver(browserName);
            default -> driver = WebDriverFactory.createWebDriver("chrome");
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }

    public List<HashMap<String,Object>> getJsonDataToMap(String filePath){
        List<HashMap<String,Object>> data;
        try {
            String jsonFile = FileUtils.readFileToString(new File(System.getProperty("user.dir") + "//src//test//resources//data//" + filePath));
            ObjectMapper objectMapper = new ObjectMapper();
            data = objectMapper.readValue(jsonFile, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    public String getTitle(){
        return driver.getTitle();
    }

    public void addToCartAndWait(ProductsPage productsPage, String name) {
        loginPage.waitUrlToBe("https://rahulshettyacademy.com/client/dashboard/dash");
        productsPage.addProductToCart(name);
        productsPage.waitForSpinnerToBeInvisible();
    }

    public String getScreenshot(WebDriver driver, String testCaseName){
        String screenshotPath = null;

        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            File source = takesScreenshot.getScreenshotAs(OutputType.FILE);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = dateFormat.format(new Date());
            String fileName = testCaseName + "_" + timestamp + ".png";

            File destination = new File(System.getProperty("user.dir") + "//reports//screenshots//" + fileName);
            FileHandler.copy(source,destination);

            String [] relativePath = destination.toString().split("reports");
            screenshotPath = ".//" + relativePath[1];

            System.out.println(screenshotPath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return screenshotPath;

    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        try {
            driver = initializeDriver();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loginPage = new LoginPage(driver);

        loginPage.goTo();

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        driver.manage().deleteAllCookies();
        driver.quit();
    }




}