package com.insove.pages;

import com.insove.config.ConfigReader;
import com.insove.utils.DriverManager;
import com.insove.utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class cho tất cả Page Object — chứa các phương thức dùng chung
 */
public abstract class BasePage {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        PageFactory.initElements(driver, this);
    }

    // ───── Navigation ─────
    protected void navigateTo(String path) {
        String url = ConfigReader.getBaseUrl() + path;
        driver.get(url);
        log.info("Điều hướng đến: {}", url);
    }

    // ───── Click ─────
    protected void click(By locator) {
        WaitUtil.waitForClickable(locator).click();
        log.debug("Click: {}", locator);
    }

    protected void click(WebElement element) {
        WaitUtil.waitForClickable(element).click();
    }

    protected void jsClick(By locator) {
        try {
            WebElement el = WaitUtil.waitForPresent(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) {
            log.warn("jsClick failed for {}: {}", locator, e.getMessage());
            throw e;
        }
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    // ───── Input ─────
    protected void type(By locator, String text) {
        WebElement el = WaitUtil.waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
        log.debug("Nhập '{}' vào: {}", text, locator);
    }

    protected void clearAndType(By locator, String text) {
        WebElement el = WaitUtil.waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    // ───── Text & Attribute ─────
    protected String getText(By locator) {
        return WaitUtil.waitForVisible(locator).getText().trim();
    }

    protected String getAttribute(By locator, String attr) {
        return WaitUtil.waitForPresent(locator).getAttribute(attr);
    }

    protected String getPlaceholder(By locator) {
        return getAttribute(locator, "placeholder");
    }

    // ───── State ─────
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ───── Select ─────
    protected void selectByText(By locator, String text) {
        new Select(WaitUtil.waitForVisible(locator)).selectByVisibleText(text);
    }

    // ───── Wait ─────
    protected void waitForUrl(String urlFragment) {
        WaitUtil.waitForUrlContains(urlFragment);
    }

    protected void waitForText(By locator, String text) {
        WaitUtil.waitForTextPresent(locator, text);
    }

    // ───── Page info ─────
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    // ───── JS Scroll ─────
    protected void scrollToElement(By locator) {
        WebElement el = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", el);
    }
}
