package com.insove.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class RegisterPage extends BasePage {

    private final By usernameField        = By.cssSelector("input[name='username'], input[placeholder='Thành']");
    private final By fullNameField        = By.cssSelector("input[placeholder='Full Name *']");
    private final By emailField           = By.cssSelector("input[placeholder='Your Email *'], input[type='email']");
    private final By phoneField           = By.cssSelector("input[placeholder='Phone Number *'], input[type='tel']");
    private final By passwordField        = By.cssSelector("form[name='register-form'] input[type='password']:nth-of-type(1)");
    private final By confirmPasswordField = By.cssSelector("input[placeholder='Confirm Password *']");
    private final By dobField             = By.cssSelector("input[type='date']");
    private final By genderSelect         = By.cssSelector("select[name='gender'], select");
    private final By registerButton       = By.cssSelector("form[name='register-form'] button[type='submit']");

    private final By loginLink            = By.cssSelector("div.text-center.mt-3 a, .contact-form ~ div a");
    private final By errorMessages        = By.cssSelector(".alert.alert-danger, .text-danger, .invalid-feedback");
    private final By pageTitle            = By.xpath("//h1[contains(.,'Đăng ký') or contains(.,'Register')]");

    public RegisterPage open() {
        navigateTo("/register");
        log.info("Mở trang Đăng ký");
        return this;
    }

    public RegisterPage enterUsername(String username) {
        type(usernameField, username);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public RegisterPage enterPhone(String phone) {
        type(phoneField, phone);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        type(passwordField, password);
        return this;
    }

    public RegisterPage enterConfirmPassword(String confirm) {
        type(confirmPasswordField, confirm);
        return this;
    }

    public void clickRegister() {
        dismissToast();
        WebElement btn = driver.findElement(registerButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", btn);
        log.info("Click nút Register (JS click)");
    }

    public void clickLoginLink() {
        dismissToast();
        WebElement link = driver.findElement(loginLink);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", link);
    }

    private void dismissToast() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('.toast,.toast-container,.Toastify').forEach(e=>e.remove())");
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    public boolean isRegisterPageDisplayed() { return isDisplayed(registerButton); }
    public boolean isPageTitleDisplayed()    { return isDisplayed(pageTitle); }
    public boolean isLoginLinkDisplayed()    { return isDisplayed(loginLink); }
    public boolean isRegisterButtonEnabled() { return isEnabled(registerButton); }
    public boolean isErrorDisplayed()        { return isDisplayed(errorMessages); }

    public String getFirstErrorMessage() {
        return isDisplayed(errorMessages) ? getText(errorMessages) : "";
    }

    public String getPasswordFieldType() {
        return getAttribute(passwordField, "type");
    }
}
