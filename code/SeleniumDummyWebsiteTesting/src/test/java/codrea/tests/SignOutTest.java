package codrea.tests;

import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import codrea.testComponents.Retry;
import org.openqa.selenium.By;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;


public class SignOutTest extends BaseTest {
    private ProductsPage productsPage;
    @Test(retryAnalyzer = Retry.class, groups = "toastValidations")
    public void signOut() throws InterruptedException {
        productsPage.headerComponent.signOut();
        Thread.sleep(800);
        Assert.assertTrue(loginPage.getToastMessage().contains("Logout Successfully"));
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");

    }
    @Test
    public void preventAccessAfterLogoutByBackButton(){
        productsPage.headerComponent.signOut();
        driver.navigate().back();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");
    }

    @Test(dataProvider = "getData")
    public void preventAccessAfterLogoutByModifyingURL(HashMap<String,String> data){
        String url = data.get("url");

        productsPage.headerComponent.signOut();
        driver.navigate().to(url);

        boolean has404StatusForURL = false;

        LogEntries logEntries = driver.manage().logs().get("browser");
        for (LogEntry entry : logEntries) {
            String logMessage = entry.getMessage();
            String resource = logMessage.split(" ")[0];

            if (resource.matches(url) && logMessage.contains("404"))
                has404StatusForURL = true;
        }


        Assert.assertEquals(getCurrentUrl(),url);
        Assert.assertEquals(getTitle(),"Not Found");
        Assert.assertTrue(driver.findElement(By.tagName("div")).getText().contains("404"));
        Assert.assertTrue(has404StatusForURL);

    }

    @DataProvider
    public Object [][] getData(){
        List<HashMap<String,Object>> hashMapList = getJsonDataToMap("preventAccess.json");
        Object[][] data = new Object[hashMapList.size()][];

        for (int i = 0; i <hashMapList.size() ; i++) {
            data[i] = new Object[] {hashMapList.get(i)};

        }
        return data;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        super.setUp();
       productsPage = loginPage.login("selenium-tester@gmail.com","Pass123@");
    }


}
