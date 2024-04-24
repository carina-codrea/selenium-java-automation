package codrea.pageObjectModel;

import codrea.component.HeaderComponent;
import codrea.utility.PageUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CartPage extends PageUtils {
    @FindBy(css = "button[routerlink='/dashboard']")
    private WebElement continueShoppingBtn;
    @FindBy(css = "div.subtotal button")
    private WebElement checkoutBtn;
    @FindBy(xpath = "//span[text()='Total']/following-sibling::span")
    private WebElement totalValue;
    @FindBy(xpath = "//h1[text()='No Products in Your Cart !']")
    private WebElement noProductsHeading;
    @FindBy(css = "ul.cartWrap")
    private List<WebElement> cartProducts;
    private final By productNameBy = By.tagName("h3");
    private final By productPriceBy = By.cssSelector("div.prodTotal");
    private final By deleteBtnBy = By.cssSelector("button[class*='btn-danger']");
    private final By buyNowBtnBy = By.xpath("//button[text()='Buy Now']");

    public HeaderComponent headerComponent;

    public CartPage(WebDriver driver) {
        super(driver);
        headerComponent = new HeaderComponent(driver);
        PageFactory.initElements(driver,this);
    }


    public List<WebElement> getCartProducts() {
        try {
          return waitAllElementsToBeVisible(cartProducts);

        } catch (TimeoutException e) {
            // If no elements are found, return an empty list
            return Collections.emptyList();
        }
    }

    public boolean isNoProductHeadingDisplayed(){
      return waitElementToBeVisible(noProductsHeading).isDisplayed();

    }

    public void continueShopping(){
        continueShoppingBtn.click();
    }

    private WebElement findProductToDelete(String productName) {
        return getCartProducts()
                .stream()
                .filter(product -> product.findElement(productNameBy).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);
    }

    private void clickDeleteButton(WebElement product) {
        product.findElement(deleteBtnBy).click();
    }


    public void deleteProduct(String productName) {
        WebElement productToDelete = findProductToDelete(productName);
        if (productToDelete != null) {
            clickDeleteButton(productToDelete);
            waitStalenessOf(productToDelete);
        } else
            throw new NoSuchElementException("Product not found or not deleted successfully: " + productName);

    }

    public boolean isProductInCart(String productName){
      return   getCartProducts()
                .stream()
                .anyMatch(product -> product.findElement(productNameBy).getText().equalsIgnoreCase(productName));

    }

    public CheckoutPage buyNow(String productName){
        getCartProducts()
                .stream()
                .filter(product -> product.findElement(productNameBy).getText().equalsIgnoreCase(productName))
                .findFirst()
                .ifPresent(product -> product.findElement(buyNowBtnBy).click());

        return new CheckoutPage(driver);
    }

    private List<Integer> getPrices(){
      List<WebElement> priceElements = waitAllElementsToBeVisible(productPriceBy);

        return priceElements.
              stream()
              .map(price -> {
                String splitPrice = price.getText().split(" ")[1];
                return Integer.parseInt(splitPrice);

              })
              .collect(Collectors.toList());

    }

    public int calculateTotal(){
        int total = 0;

        List<Integer> prices = getPrices();
        for (Integer price:prices){
            total = total + price;
        }

        return total;
    }

    public int getTotalValue(){
        String total = totalValue.getText().replace("$","");
        return Integer.parseInt(total);
    }

    public CheckoutPage goToCheckout() {
        scrollToElement(checkoutBtn);
        waitElementToBeClickable(checkoutBtn);
        click(checkoutBtn);

        return new CheckoutPage(driver);
    }

    public boolean isContinueShoppingButtonDisplayed() {
        try {
            return continueShoppingBtn.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }


}
