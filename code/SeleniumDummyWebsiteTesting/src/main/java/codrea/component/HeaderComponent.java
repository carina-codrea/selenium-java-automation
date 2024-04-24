package codrea.component;

import codrea.pageObjectModel.CartPage;
import codrea.pageObjectModel.LoginPage;
import codrea.pageObjectModel.OrdersPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.utility.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HeaderComponent extends PageUtils {

    @FindBy(css = "button[routerlink*='dashboard']")
    private WebElement homeHeader;
    @FindBy(css = "button[routerlink*='myorders']")
    private WebElement ordersHeader;
    @FindBy(css = "button[routerlink*='cart']")
    private WebElement cartHeader;
    @FindBy(xpath = "//button[text()=' Sign Out ']")
    private WebElement signOutHeader;
    @FindBy(css = "button[routerlink*='cart'] label")
    private WebElement cartCount;

    public HeaderComponent(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);

    }

    public int getCartCount(){
        int count;
        waitElementToBeVisible(cartCount);

        if (cartCount.getText().isEmpty())
            count = 0;
        else
            count = Integer.parseInt(cartCount.getText());

       return count;

    }

    public CartPage goToCart(){
        cartHeader.click();
        return  new CartPage(driver);
    }

    public OrdersPage goToOrders(){
        scrollToElement(ordersHeader);
        waitElementToBeClickable(ordersHeader);
        click(ordersHeader);
        return new OrdersPage(driver);
    }

    public ProductsPage goToHome(){
        homeHeader.click();
        return new ProductsPage(driver);
    }

    public LoginPage signOut(){
        signOutHeader.click();
        return new LoginPage(driver);
    }
}
