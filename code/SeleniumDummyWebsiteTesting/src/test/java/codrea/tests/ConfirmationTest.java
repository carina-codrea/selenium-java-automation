package codrea.tests;

import codrea.model.Order;
import codrea.pageObjectModel.CartPage;
import codrea.pageObjectModel.CheckoutPage;
import codrea.pageObjectModel.ConfirmationPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import codrea.utility.ExcelParser;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConfirmationTest extends BaseTest {

    @Test(dataProvider = "getData")
    public void productDisplayVerification(HashMap<String,Object> data){
        ProductsPage productsPage = loginPage.login((String) data.get("email"),(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder((String)data.get("country"));

        SoftAssert softAssert = new SoftAssert();
        products.forEach(product -> softAssert.assertTrue(confirmationPage.isProductPresentInOrder(product)));

        softAssert.assertAll();
    }

    @Test(dataProvider = "getData")
    public void downloadOrderDetailsInExcel(HashMap<String,Object> data) throws InterruptedException {
        ProductsPage productsPage = loginPage.login((String) data.get("email"),(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder((String)data.get("country"));
        File file = confirmationPage.downloadOrderDetailsInExcel(System.getProperty("user.dir") + "\\downloads");
        Assert.assertTrue(file.exists());
        deleteDownloadedFile(file);

    }

    @Test(dataProvider = "getData")
    public void verifyOrderDetailsConsistency(HashMap<String,Object> data) throws InterruptedException {
        String email = (String) data.get("email");
        String country = (String) data.get("country");

        ProductsPage productsPage = loginPage.login(email,(String) data.get("password"));
        List<String> products = (List<String>) data.get("products");
        products.forEach(product -> addToCartAndWait(productsPage,product));

        CartPage cartPage = productsPage.headerComponent.goToCart();

        CheckoutPage checkoutPage = cartPage.goToCheckout();
        ConfirmationPage confirmationPage = checkoutPage.placeOrder(country);

        String filePath = System.getProperty("user.dir") + "\\downloads";

        File file = confirmationPage.downloadOrderDetailsInExcel(filePath);

        List<Order> orders = confirmationPage.getOrders(products,country,email);
        List<Order> ordersFromExcel = ExcelParser.parseExcelFile(file.getAbsolutePath());


        SoftAssert softAssert = new SoftAssert();

       // Map orders from Excel by their IDs for efficient lookup
        Map<String, Order> excelOrderMap = ordersFromExcel.stream()
                .collect(Collectors.toMap(Order::getId, Function.identity()));

        for (Order webOrder : orders) {
            Order excelOrder = excelOrderMap.get(webOrder.getId());

            softAssert.assertNotNull(excelOrder, "Order from Excel not found for Order Id: " + webOrder.getId());

            softAssert.assertTrue(webOrder.equals(excelOrder, true), "Order details do not match for Order Id: " + webOrder.getId()
                    + "\n Web order: " + webOrder + "\n Excel order: " + excelOrder);
        }

        softAssert.assertAll();

        deleteDownloadedFile(file);

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


    private void deleteDownloadedFile(File file) {

        if (file.exists()) {
            if (file.delete())
                System.out.println("Downloaded file deleted successfully.");
             else
                System.out.println("Failed to delete downloaded file.");
        }
    }

}
