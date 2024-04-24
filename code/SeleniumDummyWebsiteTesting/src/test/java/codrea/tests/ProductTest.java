package codrea.tests;

import codrea.pageObjectModel.CartPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ProductTest extends BaseTest {
    private ProductsPage productsPage;


    @Test(groups = "toastValidations")
    public void addProductToCart(){
       Assert.assertTrue(loginPage.waitUrlToBe("https://rahulshettyacademy.com/client/dashboard/dash"));

       int oldCartCount = productsPage.headerComponent.getCartCount();

       productsPage.addProductToCart("ZARA COAT 3");
       productsPage.waitForSpinnerToBeInvisible();
       Assert.assertEquals(productsPage.getToastMessage(),"Product Added To Cart");

       int newCartCount = productsPage.headerComponent.getCartCount();

       Assert.assertEquals(newCartCount, oldCartCount + 1);

       CartPage cartPage = productsPage.headerComponent.goToCart();
       Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/cart");
       Assert.assertTrue(cartPage.isProductInCart("ZARA COAT 3"));

    }


    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        super.setUp();
        productsPage = loginPage.login("selenium-tester@gmail.com","Pass123@");
    }
}
