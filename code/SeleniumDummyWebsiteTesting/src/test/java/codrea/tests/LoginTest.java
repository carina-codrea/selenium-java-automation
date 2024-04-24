package codrea.tests;

import codrea.testComponents.BaseTest;
import codrea.testComponents.RandomGenerator;
import codrea.testComponents.Retry;
import codrea.utility.FormValidationUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.List;

public class LoginTest extends BaseTest {

    @Test(dataProvider = "getCorrectCredentials",groups = "toastValidations")
    public void loginWithCorrectCredentials(HashMap<String,Object> data){
        loginPage.login((String) data.get("email"),(String) data.get("password"));
        Assert.assertEquals(loginPage.getToastMessage(),"Login Successfully");
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");
    }

    @Test(dataProvider = "getCorrectCredentials" , retryAnalyzer = Retry.class,groups = "toastValidations")
    public void loginWithCorrectEmailAndIncorrectPassword(HashMap<String,Object> data){
        loginPage.login((String) data.get("email"), RandomGenerator.generateValidPassword());
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");
        Assert.assertEquals(loginPage.getToastMessage(),"Incorrect email or password.");

    }

    @Test(retryAnalyzer = Retry.class,groups = "toastValidations")
    public void loginWithIncorrectCredentials(){
       loginPage.login(RandomGenerator.generateEmail(),RandomGenerator.generateValidPassword());
       Assert.assertEquals(loginPage.getToastMessage(),"Incorrect email or password.");
       Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");
    }

    @Test(groups = "formValidations")
    public void emptySubmissionValidation(){
        FormValidationUtils formValidation = loginPage.getFormValidationUtility();

        loginPage.login("","");


        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(loginPage.getEmailInput(),"*Email is required"), "Validation message for email field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(loginPage.getPasswordInput(),"*Password is required"), "Validation message for password field is not displayed");
        softAssert.assertTrue(formValidation.isEmailInputInvalid(), "Email input field does not have red border");
        softAssert.assertTrue(formValidation.isPasswordInputInvalid(), "Password input field does not have red border");
        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");

        softAssert.assertAll();
    }

    @Test
    public void forgotPasswordLinkRedirection(){
        loginPage.clickForgotPasswordLink();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/password-new");
    }

    @Test
    public void registerLinkRedirection(){
        loginPage.clickRegisterLink();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/register");
    }

    @DataProvider
    public Object [][] getCorrectCredentials(){
        List<HashMap<String,Object>> hashMapList = getJsonDataToMap("user.json");
        Object[][] data = new Object[hashMapList.size()][];

        for (int i = 0; i <hashMapList.size() ; i++) {
            data[i] = new Object[] {hashMapList.get(i)};

        }
        return data;
    }


}
