package codrea.tests;

import codrea.pageObjectModel.CartPage;
import codrea.pageObjectModel.CheckoutPage;
import codrea.pageObjectModel.ConfirmationPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import codrea.testComponents.Retry;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.List;

public class CheckoutTest extends BaseTest {
    @Test(dataProvider = "getData")
    public void autoPopulationEmail(HashMap<String,Object> data) {
       String email = (String) data.get("email");

       ProductsPage productsPage = loginPage.login(email,(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

       CartPage cartPage = productsPage.headerComponent.goToCart();

       CheckoutPage checkoutPage = cartPage.goToCheckout();

       Assert.assertEquals(checkoutPage.getEmail(),email);

    }

    @Test(dataProvider = "getData")
    public void productDisplayVerification(HashMap<String,Object> data) {
        ProductsPage productsPage = loginPage.login((String) data.get("email"), (String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();

        SoftAssert softAssert = new SoftAssert();

        products.forEach(product -> softAssert.assertTrue(checkoutPage.isProductPresent(product),"Product with name " + product + " is not present in the checkout page."));

        softAssert.assertAll();

    }

    @Test(dataProvider = "getData",groups = "toastValidations",retryAnalyzer = Retry.class)
    public void incompleteShippingInformationHandling(HashMap<String,Object> data){
        ProductsPage productsPage = loginPage.login((String) data.get("email"),(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        checkoutPage.placeOrder("");

        Assert.assertTrue(checkoutPage.getToastMessage().contains("Please Enter Full Shipping Information"));

    }

    @Test(dataProvider = "getData", groups = "toastValidations")
    public void checkoutWithRequiredFieldsOnly(HashMap<String,Object> data) {
        ProductsPage productsPage = loginPage.login((String) data.get("email"),(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder((String) data.get("country"));
        confirmationPage.waitUrlToContain("https://rahulshettyacademy.com/client/dashboard/thanks?");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/thanks?"),"Incorrect URL after placing order.Actual:" + getCurrentUrl());
        softAssert.assertEquals(confirmationPage.getToastMessage(),"Order Placed Successfully","Incorrect toast message after placing order");

        softAssert.assertAll();

    }

    @DataProvider
    public Object[][] getData(){

        List<HashMap<String,Object>> hashMapList = getJsonDataToMap("users.json");
        Object[][] data = new Object[hashMapList.size()][];

        for (int i = 0; i <hashMapList.size() ; i++) {
            data[i] = new Object[] {hashMapList.get(i)};

        }
        return data;
    }
}
