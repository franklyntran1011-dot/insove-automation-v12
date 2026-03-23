package com.insove.tests;

import com.insove.config.ConfigReader;
import com.insove.pages.ForgotPasswordPage;
import com.insove.pages.LoginPage;
import com.insove.pages.ProfilePage;
import com.insove.pages.RegisterPage;
import com.insove.utils.DriverManager;
import com.insove.utils.WaitUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AuthenticationTest extends BaseTest {

    /** Helper: dismiss toast nếu đang che element */
    private void dismissToast() {
        try {
            ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript(
                    "document.querySelectorAll('.toast,.toast-container').forEach(e => e.remove());"
                );
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    /** Helper: chờ sau login */
    private void waitAfterAction() {
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }

    // ─── AUTH001 ───────────────────────────────────
    @Test(description = "[Auth001] Xác minh hiển thị chính xác của trang Đăng nhập")
    public void auth001_verifyLoginPageDisplay() {
        logStep("Truy cập trang /login");
        LoginPage loginPage = new LoginPage().open();

        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Trang đăng nhập không hiển thị đầy đủ");
        Assert.assertTrue(loginPage.isRegisterLinkDisplayed(),
                "Link Đăng ký không hiển thị");
        Assert.assertTrue(loginPage.isForgotPasswordLinkDisplayed(),
                "Link Quên mật khẩu không hiển thị");
        logInfo("✅ Tất cả thành phần trang đăng nhập hiển thị đúng");
    }

    // ─── AUTH004 ───────────────────────────────────
    @Test(description = "[Auth004] Xác minh placeholder text của các trường nhập liệu")
    public void auth004_verifyPlaceholderText() {
        LoginPage loginPage = new LoginPage().open();
        String userPH = loginPage.getUsernamePlaceholder();
        String passPH = loginPage.getPasswordPlaceholder();
        logInfo("Username placeholder: " + userPH);
        logInfo("Password placeholder: " + passPH);
        Assert.assertNotNull(userPH, "Không có placeholder cho username");
        Assert.assertFalse(userPH.isEmpty(), "Placeholder username trống");
        // Website dùng "Tên đăng nhập *" và "Mật khẩu *"
        Assert.assertTrue(userPH.contains("đăng nhập") || userPH.contains("Tên"),
                "Placeholder username không đúng: " + userPH);
    }

    // ─── AUTH006 ───────────────────────────────────
    @Test(description = "[Auth006] Xác minh điều hướng đến trang Đăng ký")
    public void auth006_navigateToRegister() {
        LoginPage loginPage = new LoginPage().open();
        dismissToast();
        loginPage.clickRegisterLink();
        waitAfterAction();
        String url = DriverManager.getDriver().getCurrentUrl();
        logInfo("URL: " + url);
        Assert.assertTrue(url.contains("register"),
                "Không điều hướng đến trang đăng ký. URL: " + url);
    }

    // ─── AUTH007 ───────────────────────────────────
    @Test(description = "[Auth007] Xác minh điều hướng đến trang Quên Mật Khẩu")
    public void auth007_navigateToForgotPassword() {
        LoginPage loginPage = new LoginPage().open();
        dismissToast();
        loginPage.clickForgotPasswordLink();
        waitAfterAction();
        String url = DriverManager.getDriver().getCurrentUrl();
        logInfo("URL: " + url);
        Assert.assertTrue(url.contains("forgot"),
                "Không điều hướng đến trang quên mật khẩu. URL: " + url);
    }

    // ─── AUTH011 ───────────────────────────────────
    @Test(description = "[Auth011] Xác minh đăng nhập thành công và điều hướng trang")
    public void auth011_loginSuccess() {
        LoginPage loginPage = new LoginPage().open();
        logStep("Nhập thông tin: " + ConfigReader.getValidUsername());
        loginPage.loginWith(ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // Chờ tối đa 10 giây để redirect
        logStep("Chờ redirect...");
        String url = DriverManager.getDriver().getCurrentUrl();
        for (int i = 0; i < 20; i++) {
            try { Thread.sleep(500); } catch (Exception ignored) {}
            url = DriverManager.getDriver().getCurrentUrl();
            if (!url.contains("/login")) break;
        }
        logInfo("URL sau login: " + url);

        // Kiểm tra 1: URL không còn /login
        boolean urlOk = !url.contains("/login");

        // Kiểm tra 2: Trang có avatar/tên người dùng (đã đăng nhập)
        boolean hasAvatar = false;
        try {
            hasAvatar = DriverManager.getDriver()
                .findElement(org.openqa.selenium.By.cssSelector(
                    ".dropdown.user-dropdown, .user-dropdown, [class*='user-dropdown']"))
                .isDisplayed();
        } catch (Exception ignored) {}

        logInfo("URL ok: " + urlOk + " | Avatar hiện: " + hasAvatar);
        Assert.assertTrue(urlOk || hasAvatar,
                "Đăng nhập không thành công sau 10 giây. URL: " + url);
    }

    // ─── AUTH013 ───────────────────────────────────
    @Test(description = "[Auth013] Xác minh trường mật khẩu ẩn ký tự (type=password)")
    public void auth013_passwordFieldIsHidden() {
        LoginPage loginPage = new LoginPage().open();
        String type = loginPage.getPasswordFieldType();
        logInfo("Password field type: " + type);
        Assert.assertEquals(type, "password",
                "Trường mật khẩu phải có type='password' để ẩn ký tự nhập");
    }

    // ─── AUTH017 ───────────────────────────────────
    @Test(description = "[Auth017] Xác minh đăng nhập thất bại với username không tồn tại")
    public void auth017_loginFailWrongUsername() {
        LoginPage loginPage = new LoginPage().open();
        loginPage.loginWith("UserKhongTonTai_xyz_99999", "somepassword123");
        waitAfterAction();
        boolean hasError = loginPage.isErrorDisplayed();
        boolean stayLogin = DriverManager.getDriver().getCurrentUrl().contains("/login");
        logInfo("Có lỗi: " + hasError + " | Vẫn ở login: " + stayLogin);
        Assert.assertTrue(hasError || stayLogin,
                "Hệ thống không báo lỗi với username không tồn tại");
    }

    // ─── AUTH018 ───────────────────────────────────
    @Test(description = "[Auth018] Xác minh đăng nhập thất bại với mật khẩu sai")
    public void auth018_loginFailWrongPassword() {
        LoginPage loginPage = new LoginPage().open();
        loginPage.loginWith(ConfigReader.getValidUsername(), "SaiMatKhau@999999");
        waitAfterAction();
        boolean hasError = loginPage.isErrorDisplayed();
        boolean stayLogin = DriverManager.getDriver().getCurrentUrl().contains("/login");
        logInfo("Có lỗi: " + hasError + " | Vẫn ở login: " + stayLogin);
        Assert.assertTrue(hasError || stayLogin,
                "Hệ thống không báo lỗi với mật khẩu sai");
    }

    // ─── AUTH019 ───────────────────────────────────
    @Test(description = "[Auth019] Thông báo lỗi khi để trống trường đăng nhập")
    public void auth019_loginFailEmptyFields() {
        LoginPage loginPage = new LoginPage().open();
        dismissToast();
        loginPage.clickLogin();
        waitAfterAction();
        boolean stayLogin = DriverManager.getDriver().getCurrentUrl().contains("/login");
        boolean hasError = loginPage.isErrorDisplayed();
        Assert.assertTrue(stayLogin || hasError,
                "Hệ thống không chặn đăng nhập với thông tin trống");
    }

    // ─── AUTH033 ───────────────────────────────────
    @Test(description = "[Auth033] Xác minh hiển thị chính xác trang Quên Mật Khẩu")
    public void auth033_verifyForgotPasswordPage() {
        ForgotPasswordPage page = new ForgotPasswordPage().open();
        Assert.assertTrue(page.isPageDisplayed(),
                "Trang Quên Mật Khẩu không hiển thị đầy đủ");
        logInfo("✅ Trang quên mật khẩu hiển thị đúng");
    }

    // ─── AUTH041 ───────────────────────────────────
    @Test(description = "[Auth041] Gửi yêu cầu đặt lại mật khẩu với email không tồn tại")
    public void auth041_forgotPasswordEmailNotExist() {
        ForgotPasswordPage page = new ForgotPasswordPage().open();
        page.submitEmail("email_khong_ton_tai_xyz123@notexist99.com");
        waitAfterAction();
        boolean hasResponse = page.isErrorDisplayed() || page.isSuccessMessageDisplayed();
        logInfo("Có phản hồi: " + hasResponse);
        Assert.assertTrue(hasResponse,
                "Hệ thống không có phản hồi với email không tồn tại");
    }

    // ─── AUTH042 ───────────────────────────────────
    @Test(description = "[Auth042] Gửi yêu cầu đặt lại mật khẩu khi để trống email")
    public void auth042_forgotPasswordEmptyEmail() {
        ForgotPasswordPage page = new ForgotPasswordPage().open();
        page.clickSubmit();
        waitAfterAction();
        boolean staysOnPage = DriverManager.getDriver().getCurrentUrl().contains("forgot");
        boolean hasError = page.isErrorDisplayed();
        Assert.assertTrue(staysOnPage || hasError,
                "Hệ thống không validate khi submit email trống");
    }

    // ─── AUTH043 ───────────────────────────────────
    @Test(description = "[Auth043] Gửi yêu cầu đặt lại mật khẩu với email sai định dạng")
    public void auth043_forgotPasswordInvalidEmailFormat() {
        ForgotPasswordPage page = new ForgotPasswordPage().open();
        page.submitEmail("emailsaidinhang@@@@@");
        waitAfterAction();
        boolean staysOnPage = DriverManager.getDriver().getCurrentUrl().contains("forgot");
        boolean hasError = page.isErrorDisplayed();
        Assert.assertTrue(staysOnPage || hasError,
                "Hệ thống chấp nhận email sai định dạng");
    }

    // ─── AUTH050 ───────────────────────────────────
    @Test(description = "[Auth050] Xác minh người dùng có thể đăng xuất thành công")
    public void auth050_logoutSuccess() {
        logStep("Đăng nhập");
        new LoginPage().open().loginWith(
                ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // Chờ avatar/dropdown xuất hiện trên header — chứng tỏ login thành công
        logStep("Chờ avatar header xuất hiện...");
        waitForUserDropdown();
        dismissToast();

        logStep("Đăng xuất");
        new ProfilePage().logout();
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        String url = DriverManager.getDriver().getCurrentUrl();
        logInfo("URL sau logout: " + url);
        Assert.assertTrue(
                url.contains("/login") || url.equals(ConfigReader.getBaseUrl() + "/")
                || url.equals(ConfigReader.getBaseUrl()),
                "Không điều hướng đúng sau logout. URL: " + url);
    }

    // ─── AUTH052 ───────────────────────────────────
    @Test(description = "[Auth052] Xác minh không thể truy cập trang bảo vệ sau đăng xuất")
    public void auth052_cannotAccessAfterLogout() {
        logStep("Đăng nhập");
        new LoginPage().open().loginWith(
                ConfigReader.getValidUsername(), ConfigReader.getValidPassword());

        // Chờ avatar xuất hiện
        logStep("Chờ avatar header...");
        waitForUserDropdown();
        dismissToast();

        logStep("Đăng xuất");
        new ProfilePage().logout();
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        logStep("Thử truy cập /user-info sau logout");
        DriverManager.getDriver().get(ConfigReader.getBaseUrl() + "/user-info");
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        String url = DriverManager.getDriver().getCurrentUrl();
        logInfo("URL: " + url);
        Assert.assertTrue(url.contains("/login"),
                "Có thể truy cập trang bảo vệ sau logout. URL: " + url);
    }

    /** Chờ user dropdown xuất hiện trên header — dấu hiệu login thành công */
    private void waitForUserDropdown() {
        for (int i = 0; i < 20; i++) {
            try { Thread.sleep(500); } catch (Exception ignored) {}
            try {
                boolean found = DriverManager.getDriver()
                    .findElement(org.openqa.selenium.By.cssSelector(
                        ".dropdown.user-dropdown button, .user-dropdown button"))
                    .isDisplayed();
                if (found) { logInfo("Avatar hiện sau " + (i+1)*500 + "ms"); return; }
            } catch (Exception ignored) {}
        }
        logInfo("Không tìm thấy avatar sau 10 giây");
    }

    // ─── AUTH056 ───────────────────────────────────
    @Test(description = "[Auth056] Xác minh hiển thị chính xác các thành phần trang Đăng ký")
    public void auth056_verifyRegisterPageDisplay() {
        RegisterPage page = new RegisterPage().open();
        Assert.assertTrue(page.isRegisterPageDisplayed(),
                "Trang đăng ký không hiển thị đúng");
        Assert.assertTrue(page.isLoginLinkDisplayed(),
                "Link 'Đã có tài khoản? Đăng nhập' không hiển thị");
    }

    // ─── AUTH065 ───────────────────────────────────
    @Test(description = "[Auth065] Xác minh thông báo lỗi khi để trống các trường bắt buộc khi đăng ký")
    public void auth065_registerEmptyRequiredFields() {
        RegisterPage page = new RegisterPage().open();
        dismissToast();
        page.clickRegister();
        waitAfterAction();
        boolean staysOnPage = DriverManager.getDriver().getCurrentUrl().contains("register");
        boolean hasError = page.isErrorDisplayed();
        Assert.assertTrue(staysOnPage || hasError,
                "Hệ thống không validate form đăng ký trống");
    }

    // ─── AUTH068 ───────────────────────────────────
    @Test(description = "[Auth068] Xác minh lỗi khi Password và Confirm Password không khớp")
    public void auth068_passwordMismatch() {
        RegisterPage page = new RegisterPage().open();
        page.enterPassword("Password@123456");
        page.enterConfirmPassword("DifferentPass@999");
        dismissToast();
        page.clickRegister();
        waitAfterAction();
        boolean staysOnPage = DriverManager.getDriver().getCurrentUrl().contains("register");
        boolean hasError = page.isErrorDisplayed();
        Assert.assertTrue(staysOnPage || hasError,
                "Hệ thống không báo lỗi khi password không khớp");
    }

    // ─── AUTH076 ───────────────────────────────────
    @Test(description = "[Auth076] Xác minh link 'Already have an account? Login here' điều hướng đến trang Đăng nhập")
    public void auth076_loginLinkOnRegisterPage() {
        RegisterPage page = new RegisterPage().open();
        dismissToast();
        page.clickLoginLink();
        waitAfterAction();
        String url = DriverManager.getDriver().getCurrentUrl();
        logInfo("URL: " + url);
        Assert.assertTrue(url.contains("login"),
                "Không điều hướng đến trang login. URL: " + url);
    }
}
