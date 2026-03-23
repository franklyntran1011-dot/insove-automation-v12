package com.insove.utils;

import com.insove.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Tiện ích chờ đợi phần tử — dùng Explicit Wait
 */
public class WaitUtil {
    private static final Logger log = LoggerFactory.getLogger(WaitUtil.class);

    private WaitUtil() {}

    private static WebDriverWait getWait() {
        return new WebDriverWait(
                DriverManager.getDriver(),
                Duration.ofSeconds(ConfigReader.getExplicitWait())
        );
    }

    public static WebElement waitForVisible(By locator) {
        log.debug("Chờ element visible: {}", locator);
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(By locator) {
        log.debug("Chờ element clickable: {}", locator);
        return getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(WebElement element) {
        return getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public static boolean waitForUrlContains(String text) {
        return getWait().until(ExpectedConditions.urlContains(text));
    }

    public static boolean waitForTextPresent(By locator, String text) {
        return getWait().until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    public static boolean waitForInvisible(By locator) {
        return getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static WebElement waitForPresent(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }
}
