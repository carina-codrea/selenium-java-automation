package codrea.pageObjectModel;

import codrea.component.HeaderComponent;
import codrea.model.Order;
import codrea.utility.PageUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OrdersPage extends PageUtils {

    private final By orderIdBy = By.cssSelector("table tbody th");
    private final By productNameBy = By.cssSelector("table tbody td:nth-child(3)");
    private final By productPriceBy = By.cssSelector("table tbody td:nth-child(4)");
    private final By orderDateBy = By.cssSelector("table tbody td:nth-child(5)");
    private final By deleteBtnBy = By.cssSelector("table tbody td:nth-child(7) button");
    private final By orderRowBy = By.cssSelector("table tbody tr");
    private final By noOrderShownMessage = By.cssSelector("div.mt-4");
    @FindBy(css = "button[routerlink='/dashboard']")
    private WebElement goBackToShopBtn;
    @FindBy(css = "button[routerlink='/dashboard/cart']")
    private WebElement goBackToCartBtn;


    public HeaderComponent headerComponent;


    public OrdersPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        PageFactory.initElements(driver,this);
    }

    public void goBackToShop() {
       scrollToElement(goBackToShopBtn);
       waitElementToBeClickable(goBackToShopBtn).click();

    }

    public void goBackToCart(){
        scrollToElement(goBackToCartBtn);
        waitElementToBeClickable(goBackToCartBtn).click();

    }

    public void deleteAllOrders() throws InterruptedException {
        List<WebElement> deleteBtnList;
        do {
            deleteBtnList = driver.findElements(deleteBtnBy);
            if (!deleteBtnList.isEmpty()) {
                WebElement deleteBtn = deleteBtnList.get(0);
                waitElementToBeClickable(deleteBtn).click();

                //Thread.sleep(5000);
                waitNumberOfElementsToBe(deleteBtnBy,deleteBtnList.size() - 1);
            }
        } while (!deleteBtnList.isEmpty());

    }

    public void deleteOrder(String orderId){
       List<WebElement> orders = waitAllElementsToBeVisible(orderRowBy);
       if (!orders.isEmpty()){
           orders.stream()
                   .filter(order -> order.findElement(orderIdBy).getText().equals(orderId))
                   .findFirst()
                   .ifPresent(order -> waitElementToBeClickable(order.findElement(deleteBtnBy)).click());
       }
       else
           throw new NoSuchElementException("No order with id " + orderId + " was found.");

    }


     public String getNoOrderShownMessage(){
        return waitElementToBeVisible(noOrderShownMessage).getText();
    }

    public List<Order> getOrders(String orderedBy){
        List<Order> orders = new ArrayList<>();

        List<WebElement> orderRows = waitAllElementsToBeVisible(orderRowBy);

        for (WebElement orderRow : orderRows){
            String orderId = orderRow.findElement(orderIdBy).getText();
            String productName = orderRow.findElement(productNameBy).getText();
            String productPrice = orderRow.findElement(productPriceBy).getText().split(" ")[1].trim();
            String orderDate = orderRow.findElement(orderDateBy).getText();

            Order order = new Order(orderId,productName,productPrice,orderDate);
            order.setOrderedBy(orderedBy);
            orders.add(order);
        }

        return orders;

    }

    public boolean isOrderPresent(String orderId){
        return waitAllElementsToBeVisible(orderRowBy)
                .stream()
                .anyMatch(order -> order.findElement(orderIdBy).getText().equalsIgnoreCase(orderId));

    }

    public boolean isOrderPresent(String orderId,String orderedBy){
        return getOrders()
                .stream()
                .anyMatch(order -> order.getId().equals(orderId));

    }


    public List<Order> getOrders(){
        List<Order> orders = new ArrayList<>();

        List<WebElement> orderRows = waitAllElementsToBeVisible(orderRowBy);

        for (WebElement orderRow : orderRows){
            String orderId = orderRow.findElement(orderIdBy).getText();
            String productName = orderRow.findElement(productNameBy).getText();
            String productPrice = orderRow.findElement(productPriceBy).getText().split(" ")[1].trim();
            String orderDate = orderRow.findElement(orderDateBy).getText();

            Order order = new Order(orderId,productName,productPrice,orderDate);
            orders.add(order);
        }

        return orders;

    }



}

