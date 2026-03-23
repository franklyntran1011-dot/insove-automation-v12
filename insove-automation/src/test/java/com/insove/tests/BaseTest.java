package com.insove.tests;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.insove.config.ConfigReader;
import com.insove.utils.DriverManager;
import com.insove.utils.ExtentReportManager;
import com.insove.utils.ScreenshotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Base class cho tất cả Test class
 * Xử lý: setup driver, teardown, screenshot khi fail, report
 */
public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeSuite(alwaysRun = true)
    public void initSuite() {
        ExtentReportManager.initReports();
        log.info("===== BẮT ĐẦU TEST SUITE =====");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        DriverManager.initDriver();
        String testName = method.getName();
        String description = getTestDescription(method);
        ExtentReportManager.createTest(testName, description);
        log.info("--- Bắt đầu test: {} ---", testName);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            // Chụp screenshot khi FAIL
            String screenshotPath = ScreenshotUtil.capture(result.getName());
            byte[] screenshotBytes = ScreenshotUtil.captureAsBytes();

            if (test != null) {
                test.fail("Test FAILED: " + result.getThrowable().getMessage());
                try {
                    test.addScreenCaptureFromBase64String(
                            java.util.Base64.getEncoder().encodeToString(screenshotBytes),
                            "Screenshot khi fail"
                    );
                } catch (Exception e) {
                    test.fail("Screenshot path: " + screenshotPath);
                }
                test.log(Status.FAIL, result.getThrowable());
            }
            log.error("FAIL: {} | Lỗi: {}", result.getName(), result.getThrowable().getMessage());

        } else if (result.getStatus() == ITestResult.SUCCESS) {
            if (test != null) test.pass("Test PASSED");
            log.info("PASS: {}", result.getName());

        } else if (result.getStatus() == ITestResult.SKIP) {
            if (test != null) test.skip("Test SKIPPED: " + result.getThrowable());
            log.warn("SKIP: {}", result.getName());
        }

        DriverManager.quitDriver();
        ExtentReportManager.removeTest();
    }

    @AfterSuite(alwaysRun = true)
    public void finishSuite() {
        ExtentReportManager.flushReports();
        log.info("===== KẾT THÚC TEST SUITE =====");
    }

    protected void logStep(String step) {
        log.info("  Step: {}", step);
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) test.info(step);
    }

    protected void logInfo(String info) {
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) test.info(info);
    }

    private String getTestDescription(java.lang.reflect.Method method) {
        org.testng.annotations.Test annotation = method.getAnnotation(org.testng.annotations.Test.class);
        return annotation != null && !annotation.description().isEmpty()
                ? annotation.description() : method.getName();
    }

    protected String getBaseUrl() {
        return ConfigReader.getBaseUrl();
    }
}
