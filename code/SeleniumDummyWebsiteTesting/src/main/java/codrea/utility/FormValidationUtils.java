package codrea.utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class FormValidationUtils extends PageUtils {
    private WebElement emailInput;
    private WebElement passwordInput;

    private WebElement confirmPasswordInput;
    private WebElement firstNameInput;
    private WebElement phoneNumberInput;
    private WebElement ageCheckbox;

    public FormValidationUtils(WebDriver driver) {
        super(driver);
    }

    public FormValidationUtils(WebDriver driver, WebElement emailInput, WebElement passwordInput, WebElement confirmPasswordInput) {
        super(driver);
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.confirmPasswordInput = confirmPasswordInput;
    }

    public FormValidationUtils(WebDriver driver, WebElement emailInput, WebElement passwordInput) {
        super(driver);
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
    }

    public FormValidationUtils(WebDriver driver, WebElement emailInput, WebElement passwordInput, WebElement confirmPasswordInput, WebElement firstNameInput, WebElement phoneNumberInput, WebElement ageCheckbox) {
        super(driver);
        this.emailInput = emailInput;
        this.passwordInput = passwordInput;
        this.confirmPasswordInput = confirmPasswordInput;
        this.firstNameInput = firstNameInput;
        this.phoneNumberInput = phoneNumberInput;
        this.ageCheckbox = ageCheckbox;
    }


    public boolean isEmailInputInvalid() {
        String classes = emailInput.getAttribute("class");
        return classes.contains("is-invalid");
    }

    public boolean isPasswordInputInvalid(){
        String classes = passwordInput.getAttribute("class");
        return classes.contains("is-invalid");

    }

    public boolean isConfirmPasswordInputInvalid(){
        String classes = confirmPasswordInput.getAttribute("class");
        return classes.contains("is-invalid");
    }

    public boolean isPhoneNumberInputInvalid(){
        String classes = phoneNumberInput.getAttribute("class");
        return classes.contains("is-invalid");
    }

    public boolean isFirstNameInputInvalid(){
        String classes = firstNameInput.getAttribute("class");
        return classes.contains("is-invalid");
    }

    public boolean isAgeCheckboxInvalid(){
        String classes = ageCheckbox.getAttribute("class");
        return classes.contains("is-invalid");
    }

    public boolean isMessagePresentBelowInput(WebElement element, String expectedMessage) {
        //all required fields have id instead of age checkbox

        String xpath = "";
        String id = element.getAttribute("id");

        if (!id.isEmpty())
            xpath = String.format("//input[@id='%s']/following-sibling::div[@class='invalid-feedback']",id);
        else if (element.getAttribute("type").equals("checkbox"))
                xpath = "//input[@type='checkbox']//parent::div//following-sibling::div[2]";
        else if (element.getAttribute("type").equals("email"))
                xpath = "//input[@type='email']//following-sibling::div";

        WebElement validationMessage = waitElementToBeVisible(driver.findElement(By.xpath(xpath)));

        return validationMessage.getText().contains(expectedMessage);

    }

//    public List<String> getValidationMessagesBelowInput(WebElement element) {
//        List<String> validationMessages = new ArrayList<>();
//        String xpath = "";
//        String id = element.getAttribute("id");
//
//        if (!id.isEmpty())
//            xpath = String.format("//input[@id='%s']/following-sibling::div[@class='invalid-feedback']", id);
//        else if (!element.getAttribute("type").isEmpty())
//            xpath = String.format("//input[@type='%s']/following-sibling::div[@class='invalid-feedback']", element.getAttribute("type"));
//
//        List<WebElement> validationMessageElements = driver.findElements(By.xpath(xpath));
//
//        for (WebElement messageElement : validationMessageElements) {
//            validationMessages.add(messageElement.getText());
//        }
//
//        return validationMessages;
//    }
}
