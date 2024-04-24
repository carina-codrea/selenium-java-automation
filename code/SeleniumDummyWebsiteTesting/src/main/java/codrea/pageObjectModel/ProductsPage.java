package codrea.pageObjectModel;

import codrea.component.HeaderComponent;
import codrea.utility.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductsPage extends PageUtils {
   private final By productsBy = By.cssSelector("div.card");
    private final By spinnerBy = By.tagName("ngx-spinner");
    private final By productNameBy = By.tagName("b");
    private final By addToCartBy = By.cssSelector("button.w-10");
    private final By viewBy = By.xpath("//div[@class='card']//button[text()=' View']");
    public HeaderComponent headerComponent;

    public ProductsPage(WebDriver driver){
        super(driver);
        headerComponent = new HeaderComponent(driver);
        PageFactory.initElements(driver,this);

    }

    private List<WebElement> getProducts(){
         return waitAllElementsToBeVisible(productsBy);
    }

    public WebElement getProductByName(String productName){
        return getProducts()
                .stream()
                .filter(product -> product.findElement(productNameBy).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No product found with name " + productName));
    }

    public void addProductToCart(String productName){
        getProductByName(productName).findElement(addToCartBy).click();
    }

    public void viewProduct(String productName){
        getProductByName(productName).findElement(viewBy).click();
    }

    public void waitForSpinnerToBeInvisible(){
        waitElementToBeInvisible(spinnerBy);
    }
}
