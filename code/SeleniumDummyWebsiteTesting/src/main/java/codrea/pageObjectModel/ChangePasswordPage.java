package codrea.pageObjectModel;

import codrea.utility.FormValidationUtils;
import codrea.utility.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ChangePasswordPage extends PageUtils {
    @FindBy(css = "input[type='email']")
    private WebElement emailInput;
    @FindBy(id = "userPassword")
    private WebElement passwordInput;
    @FindBy(id = "confirmPassword")
    private WebElement confirmPasswordInput;
    @FindBy(css = "button[type='submit']")
    private WebElement saveNewPasswordBtn;
    @FindBy(css = "a[href*='login']")
    private WebElement loginLink;
    @FindBy(css = "a[href*='register']")
    private WebElement registerLink;
    private final FormValidationUtils formValidationUtility;


    public ChangePasswordPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
        formValidationUtility = new FormValidationUtils(
                driver,
                emailInput,
                passwordInput,
                confirmPasswordInput);
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public WebElement getConfirmPasswordInput() {
        return confirmPasswordInput;
    }

    public FormValidationUtils getFormValidationUtility(){
        return formValidationUtility;
    }

    public LoginPage goToLogin(){
        loginLink.click();
        return new LoginPage(driver);
    }

    public RegisterPage goToRegister(){
        registerLink.click();
        return new RegisterPage(driver);
    }

    public void changePassword(String email,String password,String confirmPassword){
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        confirmPasswordInput.sendKeys(confirmPassword);
        saveNewPasswordBtn.click();
    }

}
