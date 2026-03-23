package com.insove.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Đọc file config.properties
 */
public class ConfigReader {
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class
                .getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("Không tìm thấy config.properties");
            props.load(is);
            log.info("Đã load config.properties thành công");
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc config.properties", e);
        }
    }

    public static String get(String key) {
        String value = System.getProperty(key, props.getProperty(key));
        if (value == null) log.warn("Không tìm thấy key: {}", key);
        return value;
    }

    public static String getBaseUrl()        { return get("base.url"); }
    public static String getBrowser()        { return get("browser"); }
    public static boolean isHeadless()       { return Boolean.parseBoolean(get("headless")); }
    public static int getImplicitWait()      { return Integer.parseInt(get("implicit.wait")); }
    public static int getExplicitWait()      { return Integer.parseInt(get("explicit.wait")); }
    public static int getPageLoadTimeout()   { return Integer.parseInt(get("page.load.timeout")); }
    public static String getValidUsername()  { return get("valid.username"); }
    public static String getValidPassword()  { return get("valid.password"); }
    public static String getValidEmail()     { return get("valid.email"); }
    public static String getScreenshotDir()  { return get("screenshot.dir"); }
    public static String getReportDir()      { return get("report.dir"); }
}
