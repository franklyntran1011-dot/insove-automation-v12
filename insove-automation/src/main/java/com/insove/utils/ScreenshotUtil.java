package com.insove.utils;

import com.insove.config.ConfigReader;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Chụp màn hình khi test fail — lưu vào thư mục screenshots/
 */
public class ScreenshotUtil {
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private ScreenshotUtil() {}

    /**
     * Chụp màn hình và trả về đường dẫn file (dùng để đính vào report)
     */
    public static String capture(String testName) {
        WebDriver driver = DriverManager.getDriver();
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String fileName = testName.replaceAll("[^a-zA-Z0-9_\\-]", "_")
                + "_" + timestamp + ".png";
        String dirPath = ConfigReader.getScreenshotDir();
        String filePath = dirPath + File.separator + fileName;

        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.forceMkdirParent(destFile);
            FileUtils.copyFile(srcFile, destFile);
            log.info("Screenshot đã lưu: {}", filePath);
        } catch (IOException e) {
            log.error("Lỗi khi lưu screenshot: {}", e.getMessage());
        }

        return filePath;
    }

    /**
     * Trả về byte[] để đính vào ExtentReport
     */
    public static byte[] captureAsBytes() {
        try {
            return ((TakesScreenshot) DriverManager.getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.error("Không thể chụp screenshot: {}", e.getMessage());
            return new byte[0];
        }
    }
}
