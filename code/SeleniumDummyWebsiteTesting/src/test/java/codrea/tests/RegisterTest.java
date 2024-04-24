package codrea.tests;

import codrea.enums.Gender;
import codrea.enums.Occupation;
import codrea.pageObjectModel.RegisterPage;
import codrea.testComponents.BaseTest;
import codrea.testComponents.RandomGenerator;
import codrea.testComponents.Retry;
import codrea.utility.FormValidationUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.util.HashMap;
import java.util.List;

public class RegisterTest extends BaseTest {
    private RegisterPage registerPage;
    private final String firstName = "John";
    private final String lastName = "Doe";

    @Test(retryAnalyzer = Retry.class,groups = "toastValidations")
    public void registerWithRequiredFieldsFilled() {
       String password = RandomGenerator.generateValidPassword();
       String email = RandomGenerator.generateEmail();
       String phoneNumber = RandomGenerator.generateValidPhoneNumber();
       registerPage.register(firstName,lastName,email,phoneNumber,password,password);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(registerPage.getToastMessage(),"Registered Successfully");
        softAssert.assertEquals(registerPage.getHeaderText(),"Account Created Successfully");

        registerPage.goToLogin();

        loginPage.login(email,password);
        loginPage.waitUrlToBe("https://rahulshettyacademy.com/client/dashboard/dash");

        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");

        softAssert.assertAll();
    }

    @Test(retryAnalyzer = Retry.class,groups = "toastValidations")
    public void registerWithAllFieldsFilled(){
        String password = RandomGenerator.generateValidPassword();
        String email = RandomGenerator.generateEmail();
        String phoneNumber = RandomGenerator.generateValidPhoneNumber();

        registerPage.register(firstName,lastName,email,phoneNumber,password,password, Occupation.STUDENT, Gender.FEMALE);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(registerPage.getToastMessage(),"Registered Successfully");
        softAssert.assertEquals(registerPage.getHeaderText(),"Account Created Successfully");

        registerPage.goToLogin();

        loginPage.login(email,password);
        loginPage.waitUrlToBe("https://rahulshettyacademy.com/client/dashboard/dash");

        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/dashboard/dash");

        softAssert.assertAll();

    }

    @Test(dataProvider = "getCorrectCredentials" ,retryAnalyzer = Retry.class,groups = "toastValidations")
    public void registerWithRegisteredEmail(HashMap<String,Object> data){
        String password = RandomGenerator.generateValidPassword();
        String email = (String) data.get("email");
        String phoneNumber = RandomGenerator.generateValidPhoneNumber();
        registerPage.register(firstName,lastName,email,phoneNumber,password,password);

        Assert.assertEquals(registerPage.getToastMessage(),"User already exisits with this Email Id!");
    }

    @Test(groups = "formValidations")
    public void emptySubmissionValidation() {
        registerPage.registerWithEmptyFields();

        FormValidationUtils formValidation = registerPage.getFormValidationUtility();


        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getEmailInput(),"*Email is required"), "Validation message for email field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getPasswordInput(),"*Password is required"), "Validation message for password field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getConfirmPasswordInput(),"Confirm Password is required"), "Validation message for confirm password field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getFirstNameInput(),"*First Name is required"), "Validation message for first name field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getPhoneNumberInput(),"Phone Number is required"), "Validation message for phone number field is not displayed");
        softAssert.assertTrue(formValidation.isMessagePresentBelowInput(registerPage.getAgeCheckboxInput(),"*Please check above checkbox"));
        softAssert.assertTrue(formValidation.isEmailInputInvalid(), "Email input field does not have red border");
        softAssert.assertTrue(formValidation.isPasswordInputInvalid(), "Password input field does not have red border");
        softAssert.assertTrue(formValidation.isConfirmPasswordInputInvalid(), "Confirm Password input field does not have red border");
        softAssert.assertTrue(formValidation.isFirstNameInputInvalid(), "First Name input field does not have red border");
        softAssert.assertTrue(formValidation.isPhoneNumberInputInvalid(), "Phone Number input field does not have red border");
        softAssert.assertTrue(formValidation.isAgeCheckboxInvalid());
        softAssert.assertEquals(getCurrentUrl(),"https://rahulshettyacademy.com/client/auth/register");

        softAssert.assertAll();

    }

    @Test(groups = "formValidations")
    public void shortFirstNameValidation() {
        String shortFirstName = "do";
        registerPage.enterFirstName(shortFirstName);
        registerPage.clickRegisterBtn();

        FormValidationUtils formValidationUtility = registerPage.getFormValidationUtility();
        Assert.assertTrue(formValidationUtility.isMessagePresentBelowInput(registerPage.getFirstNameInput(),"*First Name must be 3 or more character long"));
    }

    @Test(groups = "formValidations")
    public void mismatchedPasswordsValidation() {
        String password = RandomGenerator.generateValidPassword();
        String confirmPassword = RandomGenerator.generateInvalidPassword();
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.clickRegisterBtn();

        FormValidationUtils formValidationUtility = registerPage.getFormValidationUtility();
        Assert.assertTrue(formValidationUtility.isMessagePresentBelowInput(registerPage.getConfirmPasswordInput(),"Password and Confirm Password must match with each other."));
    }

    @Test(retryAnalyzer = Retry.class,groups = "toastValidations")
    public void verifyPasswordLengthToast(){
        String email = RandomGenerator.generateEmail();
        String phoneNumber = RandomGenerator.generateValidPhoneNumber();
        String password = RandomGenerator.generateShortPassword();
        registerPage.register(firstName,lastName,email,phoneNumber,password,password);
        Assert.assertEquals(registerPage.getToastMessage(),"Password must be 8 Character Long!");
    }

    @Test(groups = "toastValidations")
    public void verifyPasswordRequirementsToast(){
        String password = RandomGenerator.generateInvalidPassword();
        String email = RandomGenerator.generateEmail();
        String phoneNumber = RandomGenerator.generateValidPhoneNumber();

        registerPage.register(firstName,lastName,email,phoneNumber,password,password);

        Assert.assertEquals(registerPage.getToastMessage(),"Please enter 1 Special Character, 1 Capital 1, Numeric 1 Small");
    }

    @Test(groups = "formValidations")
    public void phoneNumberDigitsAndShortInputValidation() {
        String phoneNumber = RandomGenerator.generateInvalidPhoneNumber(false,true);
        registerPage.enterPhoneNumber(phoneNumber);
        registerPage.clickRegisterBtn();

        FormValidationUtils formValidationUtility = registerPage.getFormValidationUtility();
        Assert.assertTrue(formValidationUtility.isMessagePresentBelowInput(registerPage.getPhoneNumberInput(),"*Phone Number must be 10 digit"));
    }

    @Test(groups = "formValidations")
    public void phoneNumberDigitsAndLettersValidation() {
        String phoneNumber = RandomGenerator.generateInvalidPhoneNumber(true,false);
        registerPage.enterPhoneNumber(phoneNumber);
        registerPage.clickRegisterBtn();

        FormValidationUtils formValidationUtility = registerPage.getFormValidationUtility();
        Assert.assertTrue(formValidationUtility.isMessagePresentBelowInput(registerPage.getPhoneNumberInput(),"*only numbers is allowed"));
    }


    @Test(groups = "formValidations")
    public void phoneNumberExceedsLengthValidation() {
        String phoneNumber = RandomGenerator.generateInvalidPhoneNumber(false,false);
        System.out.println(phoneNumber);
        registerPage.enterPhoneNumber(phoneNumber);
        registerPage.clickRegisterBtn();

        FormValidationUtils formValidationUtility = registerPage.getFormValidationUtility();
        Assert.assertTrue(formValidationUtility.isMessagePresentBelowInput(registerPage.getPhoneNumberInput(),"*Phone Number must be 10 digit"));
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


    @BeforeMethod(alwaysRun = true)
    public void setUp(){
        super.setUp();
        registerPage = loginPage.clickRegisterLink();
    }
}
