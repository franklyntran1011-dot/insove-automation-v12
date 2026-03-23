package com.insove.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.insove.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Quản lý ExtentReports — tạo báo cáo HTML sau khi chạy test
 */
public class ExtentReportManager {
    private static final Logger log = LoggerFactory.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static void initReports() {
        if (extent != null) return;

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String reportPath = ConfigReader.getReportDir()
                + "/InsoveReport_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("Insove Automation Report");
        spark.config().setReportName("Insove - Website Demo6 Test Report");
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project", "Insove Medical Healthcare");
        extent.setSystemInfo("URL", ConfigReader.getBaseUrl());
        extent.setSystemInfo("Browser", ConfigReader.getBrowser());
        extent.setSystemInfo("Author", "Đăng Thành");

        log.info("ExtentReport khởi tạo tại: {}", reportPath);
    }

    public static ExtentTest createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Report đã được lưu");
        }
    }

    public static void removeTest() {
        testThreadLocal.remove();
    }
}
