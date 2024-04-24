package codrea.tests;

import codrea.pageObjectModel.ChangePasswordPage;
import codrea.pageObjectModel.ProductsPage;
import codrea.testComponents.BaseTest;
import codrea.testComponents.RandomGenerator;
import codrea.utility.FormValidationUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ChangePasswordTest extends BaseTest {
    private ChangePasswordPage changePasswordPage;

    @Test(groups = "toastValidations")
    public void changePasswordWithAssociatedEmail() {
        String password = RandomGenerator.generateValidPassword();

        changePasswordPage.changePassword("tester12@yahoo.com",password,password);

        Assert.assertEquals(loginPage.getToastMessage(),"Password Changed Successfully");
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");


       ProductsPage productsPage = loginPage.login("tester12@yahoo.com",password);
       productsPage.waitUrlToBe("https://rahulshettyacademy.com/client/dashboard/dash");
       Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");

    }

    @Test(groups = "toastValidations")
    public void changePasswordWithUnassociatedEmail(){
        String password = RandomGenerator.generateValidPassword();
        changePasswordPage.changePassword(RandomGenerator.generateEmail(),password,password);

        Assert.assertEquals(changePasswordPage.getToastMessage(),"User Not found.");
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/password-new");

    }

    @Test(groups = "formValidations")
    public void emptySubmissionValidation(){
        FormValidationUtils formValidation = changePasswordPage.getFormValidationUtility();

        changePasswordPage.changePassword("","","");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/password-new");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(changePasswordPage.getEmailInput(),"*Email is required"), "Validation message for email field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(changePasswordPage.getPasswordInput(),"*Password is required"), "Validation message for password field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(changePasswordPage.getConfirmPasswordInput(),"*Confirm Password is required"), "Validation message for confirm password field is not displayed");
        softAssert.assertTrue(formValidation.isEmailInputInvalid(), "Email input field does not have red border");
        softAssert.assertTrue(formValidation.isPasswordInputInvalid(), "Password input field does not have red border");
        softAssert.assertTrue(formValidation.isConfirmPasswordInputInvalid(),"Confirm password field does not have red border");

        softAssert.assertAll();
    }

    @Test(groups = "formValidations")
    public void changePasswordWithMismatchedPasswords(){
        FormValidationUtils formValidation = changePasswordPage.getFormValidationUtility();

        changePasswordPage.changePassword("tester12@yahoo.com",RandomGenerator.generateValidPassword(),RandomGenerator.generateValidPassword());

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/password-new");
        softAssert.assertTrue(formValidation.isConfirmPasswordInputInvalid());
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(changePasswordPage.getConfirmPasswordInput(),"Password and Confirm Password must match with each other."));
        softAssert.assertAll();
    }

    @Test
    public void registerRedirectionLink(){
        changePasswordPage.goToRegister();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/register");
    }

    @Test
    public void loginRedirectionLink(){
        changePasswordPage.goToLogin();
        Assert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/login");

    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        super.setUp();
        changePasswordPage = loginPage.clickForgotPasswordLink();
    }
}
