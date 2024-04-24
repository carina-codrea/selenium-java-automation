package codrea.tests;

import codrea.pageObjectModel.CartPage;
import codrea.pageObjectModel.CheckoutPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.List;

public class CartTest extends BaseTest {
    private ProductsPage productsPage;

    @Test(groups = "toastValidations")
    public void noProductsInCartMessageDisplay(){
        CartPage cartPage = productsPage.headerComponent.goToCart();
        Assert.assertEquals(driver.getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/cart");
        Assert.assertTrue(cartPage.isNoProductHeadingDisplayed());
        Assert.assertTrue(cartPage.getToastMessage().contains("No Product in Your Cart"));

    }
    @Test
    public void redirectToHomepageAndButtonVisibility(){
        CartPage cartPage = productsPage.headerComponent.goToCart();
        Assert.assertTrue(cartPage.isContinueShoppingButtonDisplayed());
        cartPage.continueShopping();
        Assert.assertEquals(driver.getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");
    }
    @Test(dataProvider = "getProducts")
    public void redirectToCheckout(HashMap<String,Object> data) {
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));
        CartPage cartPage = productsPage.headerComponent.goToCart();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/cart");
        cartPage.goToCheckout();
        Assert.assertTrue(getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order?prop="));

    }

    @Test(dataProvider = "getProducts")
    public void verifyCheckoutPageAfterBuyNowButtonClick (HashMap<String,Object> data){
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));
        CartPage cartPage = productsPage.headerComponent.goToCart();

        String selectedProduct = products.get(0);

        CheckoutPage checkoutPage = cartPage.buyNow(selectedProduct);
        Assert.assertTrue(getCurrentUrl().contains("https://rahulshettyacademy.com/client/dashboard/order"));
        Assert.assertTrue(checkoutPage.isOnlyProduct(selectedProduct));
    }

    @Test
    public void deleteProduct() {
        addToCartAndWait(productsPage,"ZARA COAT 3");
        addToCartAndWait(productsPage,"ADIDAS ORIGINAL");

        int oldCountCart = productsPage.headerComponent.getCartCount();

        CartPage cartPage = productsPage.headerComponent.goToCart();
        cartPage.deleteProduct("ZARA COAT 3");


        int newCountCart = productsPage.headerComponent.getCartCount();

        Assert.assertFalse(cartPage.isProductInCart("ZARA COAT 3"));
        Assert.assertEquals(newCountCart,oldCountCart - 1);

    }

    @Test(groups = "toastValidations")
    public void deleteOnlyProduct() {
        String productName = "ZARA COAT 3";

        addToCartAndWait(productsPage,productName);

        int oldCountCart = productsPage.headerComponent.getCartCount();

        CartPage cartPage = productsPage.headerComponent.goToCart();
        cartPage.deleteProduct(productName);

        int newCountCart = productsPage.headerComponent.getCartCount();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(newCountCart,oldCountCart - 1);
        softAssert.assertTrue(cartPage.isNoProductHeadingDisplayed());
        softAssert.assertEquals(cartPage.getToastMessage(),"No Product in Your Cart");
        softAssert.assertTrue(cartPage.getCartProducts().isEmpty(),"The cart is not empty.");

        softAssert.assertAll();

    }


    @Test(dataProvider = "getProducts")
    public void ensureTotalValueAccuracy(HashMap<String,Object> data){
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        Assert.assertEquals(cartPage.getTotalValue(),cartPage.calculateTotal());
    }

    @Test(groups = "failure")
    public void totalValueAfterDeleteProductFromCart(){
        addToCartAndWait(productsPage,"ZARA COAT 3");
        addToCartAndWait(productsPage,"ADIDAS ORIGINAL");

        CartPage cartPage = productsPage.headerComponent.goToCart();

        Assert.assertEquals(cartPage.getTotalValue(),cartPage.calculateTotal());

        cartPage.deleteProduct("ZARA COAT 3");

        Assert.assertEquals(cartPage.getTotalValue(),cartPage.calculateTotal());

    }



    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        super.setUp();
        productsPage = loginPage.login("selenium-tester@gmail.com","Pass123@");

    }

    @DataProvider
    public Object [][] getProducts(){
        List<HashMap<String,Object>> hashMapList = getJsonDataToMap("products.json");
        Object[][] data = new Object[hashMapList.size()][];

        for (int i = 0; i <hashMapList.size() ; i++) {
            data[i] = new Object[] {hashMapList.get(i)};

        }
        return data;
    }

  }
