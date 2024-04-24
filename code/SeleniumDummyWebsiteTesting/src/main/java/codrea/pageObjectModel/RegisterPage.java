package codrea.pageObjectModel;

import codrea.enums.Gender;
import codrea.enums.Occupation;
import codrea.utility.FormValidationUtils;
import codrea.utility.PageUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class RegisterPage extends PageUtils {
    @FindBy(id ="firstName")
    private WebElement firstNameInput;
    @FindBy(id = "lastName")
    private WebElement lastNameInput;
    @FindBy(id = "userEmail")
    private WebElement emailInput;
    @FindBy(id = "userMobile")
    private WebElement phoneNumberInput;
    @FindBy(css = "select[formcontrolname='occupation']")
    private WebElement occupationSelect;
    @FindBy(id = "userPassword")
    private WebElement passwordInput;
    @FindBy(id = "confirmPassword")
    private WebElement confirmPasswordInput;
    @FindBy(css = "input[type='checkbox']")
    private WebElement ageCheckboxInput;
    @FindBy(id = "login")
    private WebElement registerBtn;
    @FindBy(css = "input[value='Male']")
    private WebElement maleRadioBtn;
    @FindBy(css = "input[value='Female']")
    private WebElement femaleRadioBtn;
    @FindBy(css = "h1.headcolor")
    private WebElement accountCreatingHeading;
    @FindBy(css = "button[routerlink='/auth']")
    private WebElement loginBtn;
    private final FormValidationUtils formValidationUtility;


    public RegisterPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver,this);
        formValidationUtility = new FormValidationUtils(
                driver,
                emailInput,
                passwordInput,
                confirmPasswordInput,
                firstNameInput,
                phoneNumberInput,
                ageCheckboxInput);
    }

    public WebElement getFirstNameInput(){
        return firstNameInput;
    }
    public WebElement getPasswordInput(){
        return passwordInput;
    }

    public WebElement getConfirmPasswordInput(){
        return confirmPasswordInput;
    }
    public WebElement getEmailInput(){
        return emailInput;
    }

    public WebElement getPhoneNumberInput(){
        return phoneNumberInput;
    }

    public WebElement getAgeCheckboxInput(){
        return ageCheckboxInput;
    }


    public FormValidationUtils getFormValidationUtility(){
        return formValidationUtility;
    }

    public void register(String firstName,
                         String lastName,
                         String email,
                         String phoneNumber,
                         String password,
                         String confirmPassword)
    {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhoneNumber(phoneNumber);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        confirmAge();
        clickRegisterBtn();

    }

    public void register(String firstName,
                         String lastName,
                         String email,
                         String phoneNumber,
                         String password,
                         String confirmPassword,
                         Occupation occupation,
                         Gender gender)
    {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPhoneNumber(phoneNumber);
        selectOccupation(occupation);
        selectGender(gender);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        confirmAge();
        clickRegisterBtn();

    }

    public void registerWithEmptyFields() {
        clickRegisterBtn();

    }

    public void goToLogin(){
        loginBtn.click();
    }

    public void enterFirstName(String firstName){
        firstNameInput.sendKeys(firstName);
    }

    public void enterLastName(String lastName){
        lastNameInput.sendKeys(lastName);
    }

    public void enterEmail(String email){
        emailInput.sendKeys(email);
    }

    public void enterPhoneNumber(String phoneNumber){
        phoneNumberInput.sendKeys(phoneNumber);
    }

    public void enterPassword(String password){
        passwordInput.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword){
        confirmPasswordInput.sendKeys(confirmPassword);
    }

    public void confirmAge(){
        ageCheckboxInput.click();
    }

    public void clickRegisterBtn(){
        registerBtn.click();
    }
    public void selectOccupation(Occupation occupation){
        Select select = new Select(occupationSelect);
        select.selectByVisibleText(occupation.getDisplayName());
    }

    public void selectGender(Gender gender){
       if (gender == Gender.FEMALE)
           femaleRadioBtn.click();
       else if (gender == Gender.MALE)
           maleRadioBtn.click();
    }

    public String getHeaderText(){
        return waitElementToBeVisible(accountCreatingHeading).getText();
    }

}
