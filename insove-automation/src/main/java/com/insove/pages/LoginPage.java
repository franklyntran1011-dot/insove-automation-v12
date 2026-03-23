package com.insove.pages;

import com.insove.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    private final By usernameField  = By.cssSelector("input[name='username']");
    private final By passwordField  = By.cssSelector("input[name='password']");
    private final By loginButton    = By.cssSelector("button.btn.btn-primary.btn-pill.btn-lg");
    private final By registerLink   = By.xpath("//a[contains(.,'Đăng ký tại đây')]");
    private final By forgotPassLink = By.cssSelector("a[href='/forgot-password']");
    private final By errorMessage   = By.cssSelector(".alert-danger,.text-danger,.invalid-feedback,.toast-body");
    private final By pageTitle      = By.xpath("//h1[contains(.,'Đăng nhập')]");
    private final By breadcrumb     = By.cssSelector(".breadcrumb");

    public LoginPage open() {
        navigateTo("/login");
        log.info("Mở trang /login");
        WaitUtil.waitForVisible(usernameField);
        return this;
    }

    public LoginPage enterUsername(String username) {
        dismissToast();
        WebElement el = WaitUtil.waitForVisible(usernameField);
        el.clear();
        el.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement el = WaitUtil.waitForVisible(passwordField);
        el.clear();
        el.sendKeys(password);
        return this;
    }

    public void clickLogin() {
        dismissToast();
        try {
            WebElement btn = WaitUtil.waitForClickable(loginButton);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'})", btn);
            try { Thread.sleep(200); } catch (Exception ignored) {}
            btn.click();
            log.info("Click nút Đăng nhập");
        } catch (Exception e) {
            log.warn("Click failed, thử Enter: {}", e.getMessage());
            try {
                driver.findElement(passwordField).sendKeys(Keys.ENTER);
            } catch (Exception e2) {
                log.error("Không submit được form login");
            }
        }
    }

    public void loginWith(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public void clickRegisterLink() {
        dismissToast();
        jsClick(registerLink);
    }

    public void clickForgotPasswordLink() {
        dismissToast();
        jsClick(forgotPassLink);
    }

    private void dismissToast() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('.toast,.toast-container,.Toastify').forEach(e=>e.remove())");
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(usernameField) && isDisplayed(passwordField);
    }

    public boolean isRegisterLinkDisplayed()       { return isDisplayed(registerLink); }
    public boolean isForgotPasswordLinkDisplayed() { return isDisplayed(forgotPassLink); }
    public boolean isPageTitleDisplayed()           { return isDisplayed(pageTitle); }
    public boolean isBreadcrumbDisplayed()          { return isDisplayed(breadcrumb); }

    public boolean isErrorDisplayed() {
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        return isDisplayed(errorMessage)
            || isDisplayed(By.cssSelector(".alert,[class*='error'],[class*='danger']"));
    }

    public String getErrorMessage() {
        return isDisplayed(errorMessage) ? getText(errorMessage) : "";
    }

    public boolean isLoginButtonEnabled()  { return isEnabled(loginButton); }
    public String getUsernamePlaceholder() { return getPlaceholder(usernameField); }
    public String getPasswordPlaceholder() { return getPlaceholder(passwordField); }
    public String getPasswordFieldType()   { return getAttribute(passwordField, "type"); }
}
