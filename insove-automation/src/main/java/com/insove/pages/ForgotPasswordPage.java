package com.insove.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

/**
 * Page Object cho trang Quên Mật Khẩu
 * URL: /forgot-password
 */
public class ForgotPasswordPage extends BasePage {

    private final By emailField   = By.cssSelector("input[type='email'], input[name='email']");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By successMsg   = By.cssSelector(".alert-success, .text-success");
    private final By errorMsg     = By.cssSelector(".alert-danger, .text-danger, .invalid-feedback");
    private final By pageTitle    = By.xpath("//h1[contains(.,'Quên') or contains(.,'Forgot')]");
    private final By resendButton = By.xpath("//button[contains(.,'Gửi lại') or contains(.,'Resend')]");

    public ForgotPasswordPage open() {
        navigateTo("/forgot-password");
        log.info("Mở trang Quên Mật Khẩu");
        return this;
    }

    public ForgotPasswordPage enterEmail(String email) {
        type(emailField, email);
        return this;
    }

    public void clickSubmit() {
        dismissToast();
        WebElement btn = driver.findElement(submitButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", btn);
    }

    public void submitEmail(String email) {
        enterEmail(email);
        clickSubmit();
    }

    private void dismissToast() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('.toast,.toast-container,.Toastify').forEach(e=>e.remove())");
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    public boolean isPageDisplayed()            { return isDisplayed(emailField) && isDisplayed(submitButton); }
    public boolean isPageTitleDisplayed()       { return isDisplayed(pageTitle); }
    public boolean isSuccessMessageDisplayed()  { return isDisplayed(successMsg); }
    public boolean isErrorDisplayed()           { return isDisplayed(errorMsg); }
    public String  getErrorMessage()            { return isDisplayed(errorMsg) ? getText(errorMsg) : ""; }
    public boolean isResendButtonDisplayed()    { return isDisplayed(resendButton); }
    public String  getEmailPlaceholder()        { return getPlaceholder(emailField); }
}
