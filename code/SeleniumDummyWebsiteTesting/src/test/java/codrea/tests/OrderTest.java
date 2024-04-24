package codrea.tests;

import codrea.model.Order;
import codrea.pageObjectModel.*;
import codrea.testComponents.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.List;

public class OrderTest extends BaseTest {

    @Test(dataProvider = "userCredentialsProvider")
    public void redirectToHomepage(HashMap<String,Object> data) {
      ProductsPage productsPage = loginPage.login((String)data.get("email"),(String) data.get("password"));
      OrdersPage ordersPage = productsPage.headerComponent.goToOrders();
      ordersPage.goBackToShop();

      Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");

    }

    @Test(dataProvider = "userCredentialsProvider")
    public void redirectToCart(HashMap<String,Object> data){
        ProductsPage productsPage = loginPage.login((String)data.get("email"),(String) data.get("password"));
        OrdersPage ordersPage = productsPage.headerComponent.goToOrders();
        ordersPage.goBackToCart();

        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/cart");
    }

    @Test(dataProvider = "getData")
    public void noOrdersMessageDisplayed(HashMap<String,Object> data) throws InterruptedException {
        ProductsPage productsPage = loginPage.login((String)data.get("email"),(String) data.get("password"));
        OrdersPage ordersPage = productsPage.headerComponent.goToOrders();
        ordersPage.deleteAllOrders();

        Assert.assertTrue(ordersPage.getNoOrderShownMessage().contains("You have No Orders to show at this time."));
    }

    @Test(dataProvider = "getData")
    public void  verifyOrderPresenceAfterPlacement(HashMap<String,Object> data) {
        String email = (String) data.get("email");
        String country = (String) data.get("country");

        ProductsPage productsPage = loginPage.login(email, (String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage, product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder(country);
        confirmationPage.waitUrlToContain("https://rahulshettyacademy.com/client/dashboard/thanks?");

        List<Order> ordersFromConfirmationPage = confirmationPage.getOrders(products,country,email);

        OrdersPage ordersPage = confirmationPage.headerComponent.goToOrders();
        List<Order> ordersFromOrdersPage = ordersPage.getOrders(email);

        SoftAssert softAssert = new SoftAssert();

        for (Order placedOrder : ordersFromConfirmationPage) {
            boolean found = false;
            for (Order order : ordersFromOrdersPage) {
                if (order.equals(placedOrder,false)) {
                    found = true;
                    break;
                }
            }

            softAssert.assertTrue(found, "Order from confirmation page is not present in orders from orders page: " + placedOrder);
        }

        softAssert.assertAll();

    }

    @Test(dataProvider = "getData",groups = "toastValidations")
    public void deleteOrder(HashMap<String,Object> data) throws InterruptedException {
        String email = (String) data.get("email");
        String country = (String) data.get("country");

        ProductsPage productsPage = loginPage.login(email, (String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage, product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder(country);
        confirmationPage.waitUrlToContain("https://rahulshettyacademy.com/client/dashboard/thanks?");

        List<Order> ordersFromConfirmationPage = confirmationPage.getOrders(products,country,email);

         String orderId = ordersFromConfirmationPage.get(0).getId();

        OrdersPage ordersPage = confirmationPage.headerComponent.goToOrders();

        //the orders table is not stable,rows are changing their position,we wait for the table to stabilize
        Thread.sleep(2500);

        ordersPage.deleteOrder(orderId);

        Assert.assertTrue(ordersPage.getToastMessage().contains("Orders Deleted Successfully"));

        Thread.sleep(7000);
        Assert.assertFalse(ordersPage.isOrderPresent(orderId,(String) data.get("email")),"Order with id " + orderId + " wasn't deleted.");
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


    @DataProvider
    public Object[][] userCredentialsProvider(){

        List<HashMap<String,Object>> hashMapList = getJsonDataToMap("user.json");
        Object[][] data = new Object[hashMapList.size()][];

        for (int i = 0; i <hashMapList.size() ; i++) {
            data[i] = new Object[] {hashMapList.get(i)};

        }
        return data;
    }

}
