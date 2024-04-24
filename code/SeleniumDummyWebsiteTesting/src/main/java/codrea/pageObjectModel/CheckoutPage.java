package codrea.pageObjectModel;

import codrea.utility.PageUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CheckoutPage extends PageUtils {
    @FindBy(css = "div.details__item")
    private List<WebElement> checkoutProducts;
    @FindBy(css = "div[class*='user__name']>input")
    private WebElement emailInput;
    @FindBy(css = "input[placeholder='Select Country']")
    private WebElement selectCountryInput;
    @FindBy(css = "div.actions a")
    private WebElement placeOrderBtn;

    private final By suggestedCountriesBy = By.cssSelector("button.ta-item");
    private final By productNameBy = By.cssSelector("div.item__title");
    public CheckoutPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
    }

    public List<WebElement> getCheckoutProducts(){
        waitAllElementsToBeVisible(checkoutProducts);
        return checkoutProducts;
    }

    public boolean isProductPresent(String productName){
        return getCheckoutProducts()
                .stream()
                .anyMatch(product -> product.findElement(productNameBy).getText().equalsIgnoreCase(productName));

    }
    public boolean isOnlyProduct(String productName){
        List<WebElement> checkoutProducts = getCheckoutProducts();
        return checkoutProducts.size() == 1 && (checkoutProducts.get(0).findElement(productNameBy).getText().equalsIgnoreCase(productName));
    }

    public String getEmail(){
       return waitElementToBeVisible(emailInput).getAttribute("value");

    }

    private List<WebElement> getSuggestedCountries(){
        try {
            return waitAllElementsToBeVisible(suggestedCountriesBy);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("No suggested countries dropdown appeared.");
        }

    }

    private void selectCountry(String country){
        if (!country.isEmpty()) {
            selectCountryInput.sendKeys(country);
            Optional<WebElement> matchingCountry = getSuggestedCountries()
                    .stream()
                    .filter(suggestedCountry -> suggestedCountry.getText().equalsIgnoreCase(country))
                    .findFirst();

            if (matchingCountry.isPresent())
                matchingCountry.get().click();
            else
                throw new NoSuchElementException("No country with name " + country + " was not found.");
        }
        }

    public ConfirmationPage placeOrder(String country) {
            selectCountry(country);
            this.scrollToElement(placeOrderBtn);
            waitElementToBeClickable(placeOrderBtn).click();
            return new ConfirmationPage(driver);
    }
}
