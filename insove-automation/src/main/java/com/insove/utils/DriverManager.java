package com.insove.utils;

import com.insove.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Quản lý WebDriver instance, dùng ThreadLocal để hỗ trợ parallel test
 */
public class DriverManager {
    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static void initDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase().trim();
        boolean headless = ConfigReader.isHeadless();
        WebDriver driver;

        log.info("Khởi tạo driver: {} | headless: {}", browser, headless);

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (headless) ffOpts.addArguments("--headless");
                driver = new FirefoxDriver(ffOpts);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOpts = new EdgeOptions();
                if (headless) edgeOpts.addArguments("--headless");
                driver = new EdgeDriver(edgeOpts);
                break;
            default: // chrome
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOpts = new ChromeOptions();
                if (headless) {
                    chromeOpts.addArguments("--headless=new");
                    chromeOpts.addArguments("--no-sandbox");
                    chromeOpts.addArguments("--disable-dev-shm-usage");
                }
                chromeOpts.addArguments("--window-size=1920,1080");
                chromeOpts.addArguments("--disable-notifications");
                chromeOpts.addArguments("--disable-popup-blocking");
                driver = new ChromeDriver(chromeOpts);
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getPageLoadTimeout()));

        driverThreadLocal.set(driver);
        log.info("Driver khởi tạo thành công");
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) throw new IllegalStateException("Driver chưa được khởi tạo. Gọi initDriver() trước.");
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("Driver đã được đóng");
        }
    }
}
