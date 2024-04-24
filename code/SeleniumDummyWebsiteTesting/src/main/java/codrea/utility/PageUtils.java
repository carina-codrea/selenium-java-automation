package codrea.utility;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.time.Duration;
import java.util.List;

public class PageUtils {
    public WebDriver driver;
    private WebDriverWait wait;


    @FindBy(id = "toast-container")
    private WebElement toastContainer;

    public PageUtils(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver,Duration.ofSeconds(5));
        PageFactory.initElements(driver,this);
    }

    public WebElement waitElementToBeVisible(WebElement element){
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitElementToBeVisible(By locator){
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitElementToBeInvisible(By locator){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public List<WebElement> waitAllElementsToBeVisible(By locator){
       return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
    public List<WebElement> waitAllElementsToBeVisible(List<WebElement> elementList){
         wait.until(ExpectedConditions.visibilityOfAllElements(elementList));
        return elementList;
    }

    public WebElement waitElementToBeClickable(WebElement element){
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public boolean waitUrlToBe(String url){
      return wait.until(ExpectedConditions.urlToBe(url));
    }

    public void waitUrlToContain(String url){
        wait.until(ExpectedConditions.urlContains(url));
    }

    public File waitForFileDownload(String directory) throws InterruptedException {
        Thread.sleep(3000);

        File dir = new File(directory);
        File [] files = dir.listFiles(file -> file.getName().endsWith(".xlsx"));

        if (files == null || files.length == 0)
            throw  new RuntimeException("No .xlsx files found in the directory " + directory);
        return files[0];
    }


    public void scrollToElement(WebElement element){
        Point point = element.getLocation();
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollTo(arguments[0],arguments[1])", point.getX(), point.getY());

    }

    public String getToastMessage(){
        wait.until(ExpectedConditions.visibilityOf(toastContainer));
        return toastContainer.getText();
    }

    public void click(WebElement element){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].click();", element);
    }

    public void waitNumberOfElementsToBe(By locator , int size){
        wait.until(ExpectedConditions.numberOfElementsToBe(locator,size));
    }

  public void waitStalenessOf(WebElement element){
        wait.until(ExpectedConditions.stalenessOf(element));
  }

}
