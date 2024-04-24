package codrea.pageObjectModel;

import codrea.utility.FormValidationUtils;
import codrea.utility.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends PageUtils {
    @FindBy(id = "userEmail")
    private WebElement emailInput;
    @FindBy(id = "userPassword")
    private WebElement passwordInput;
    @FindBy(id = "login")
    private WebElement loginBtn;
    @FindBy(css = "a.forgot-password-link")
    private WebElement forgotPasswordLink;
    @FindBy(css ="p[class*='footer-text']")
    private WebElement registerLink;
    private final FormValidationUtils formValidationUtility;

    public LoginPage(WebDriver driver){
        super(driver);
        PageFactory.initElements(driver,this);
        formValidationUtility = new FormValidationUtils(
                driver,
                emailInput,
                passwordInput
        );
    }

    public FormValidationUtils getFormValidationUtility(){
        return formValidationUtility;
    }

    public WebElement getEmailInput() {
        return emailInput;
    }

    public WebElement getPasswordInput() {
        return passwordInput;
    }

    public void goTo(){
        driver.get("https://rahulshettyacademy.com/client/");
    }

    public ProductsPage login(String email,String password){
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        loginBtn.click();

        return new ProductsPage(driver);
    }

    public RegisterPage clickRegisterLink(){
        registerLink.click();
        return new RegisterPage(driver);
    }
    public ChangePasswordPage clickForgotPasswordLink(){
        forgotPasswordLink.click();
        return new ChangePasswordPage(driver);
    }

}
