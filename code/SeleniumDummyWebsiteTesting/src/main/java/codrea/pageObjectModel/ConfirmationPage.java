package codrea.pageObjectModel;

import codrea.component.HeaderComponent;
import codrea.model.Order;
import codrea.utility.DateUtils;
import codrea.utility.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfirmationPage extends PageUtils {
    @FindBy(css = "td.box table tr:nth-child(3) label")
    private List<WebElement> ordersId;
    @FindBy(css = "tr.line-item")
    private List<WebElement> orderedProducts;
    private final By productNameBy = By.cssSelector("tr.line-item td:nth-child(2) div.title");
    private final By productPriceBy = By.cssSelector("tr.line-item td:nth-child(3) div.title");
    @FindBy(xpath = "//button[text()='Click To Download Order Details in Excel']")
    private WebElement downloadOrderInExcelBtn;

    public HeaderComponent headerComponent;

    public ConfirmationPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        PageFactory.initElements(driver,this);
    }


    //orderID's from ConfirmationPage will be assigned in the order of which the products where added to the cart by the user
    // we get the list of the products added by the user
    //then we sort the orders from the Confirmation Page by product name to match the order of the products added by the user list
    public List<Order> getOrders(List<String> userAddedProducts, String address,String orderedBy){
        List<WebElement> orderedProductList =  waitAllElementsToBeVisible(orderedProducts);

        List<WebElement> sortedOrderList = orderedProductList
                .stream()
                        .sorted(Comparator.comparing(product -> {
                            String productName = product.findElement(productNameBy).getText();
                            return userAddedProducts.indexOf(productName);
                        }))
                                .collect(Collectors.toList());


        waitAllElementsToBeVisible(ordersId);

        List<Order> orders = new ArrayList<>();

        int numOrders = ordersId.size();

        if (numOrders != sortedOrderList.size())
            throw new IllegalStateException("Size of list doesn't match.");


        for (int i = 0; i < numOrders ; i++) {
            String orderId = ordersId.get(i).getText().split("\\|")[1].trim();
            String productName = sortedOrderList.get(i).findElement(productNameBy).getText();
            String productPrice = sortedOrderList.get(i).findElement(productPriceBy).getText().split(" ")[1].trim();
            String date = DateUtils.getCurrentFormattedDate();

            Order order = new Order(orderId,productName,productPrice,date,address,orderedBy);
            orders.add(order);
        }
        return orders;
    }


    private List<WebElement> getOrderedProducts(){
      return   waitAllElementsToBeVisible(productNameBy);
    }


    public boolean isProductPresentInOrder(String productName){
       return getOrderedProducts()
                .stream()
                .anyMatch(product -> product.getText().equalsIgnoreCase(productName));

    }

    public File downloadOrderDetailsInExcel(String directoryPath) throws InterruptedException {
        waitUrlToContain("https://rahulshettyacademy.com/client/dashboard/thanks?");
        downloadOrderInExcelBtn.click();
        return waitForFileDownload(directoryPath);

    }
}
