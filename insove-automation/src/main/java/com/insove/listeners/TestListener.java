package com.insove.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.insove.utils.ExtentReportManager;
import com.insove.utils.ScreenshotUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Base64;

public class TestListener implements ITestListener {
    private static final Logger log = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("========== BẮT ĐẦU: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("========== KẾT THÚC: {} | Pass: {} | Fail: {} | Skip: {} ==========",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶ BẮT ĐẦU TEST: {}", result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.INFO, "Bắt đầu test: " + result.getName());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✅ PASS: {}", result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.PASS, "Test PASSED ✅");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("❌ FAIL: {} | Lỗi: {}", result.getName(), result.getThrowable().getMessage());

        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            // Chụp và đính screenshot vào report
            try {
                byte[] screenshotBytes = ScreenshotUtil.captureAsBytes();
                if (screenshotBytes.length > 0) {
                    String base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);
                    test.addScreenCaptureFromBase64String(base64Screenshot, "Screenshot lúc FAIL");
                }
            } catch (Exception e) {
                log.warn("Không thể chụp screenshot: {}", e.getMessage());
            }

            test.log(Status.FAIL, "Test FAILED ❌");
            test.log(Status.FAIL, result.getThrowable());
        }

        // Lưu screenshot vào thư mục
        ScreenshotUtil.capture("FAIL_" + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⚠ SKIP: {}", result.getName());
        ExtentTest test = ExtentReportManager.getTest();
        if (test != null) {
            test.log(Status.SKIP, "Test SKIPPED ⚠");
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        log.warn("Partial FAIL: {}", result.getName());
    }
}
