package com.insove.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ProfilePage extends BasePage {

    private final By avatar           = By.cssSelector("[style*='border-radius: 50%'],[style*='border-radius:50%'],[class*='avatar'],[class*='Avatar']");
    private final By userName         = By.cssSelector("[class*='user-name'],[class*='username'],[class*='profile-name'],[class*='name'] h4,[class*='name'] h5");
    private final By userEmail        = By.cssSelector("[class*='user-email'],[class*='profile-email'],[class*='email'] p,[class*='email'] span");
    private final By phoneLabel       = By.cssSelector("[class*='phone'],[class*='Phone'],[class*='tel']");
    private final By addressLabel     = By.cssSelector("[class*='address'],[class*='Address'],[class*='location']");
    private final By joinDateLabel    = By.cssSelector("[class*='join'],[class*='Join'],[class*='date'],[class*='member']");

    private final By appointmentsTab  = By.cssSelector("a[href*='appointment'],button[data-tab*='appointment'],[class*='tab']:nth-child(1)");
    private final By waitlistTab      = By.cssSelector("a[href*='waitlist'],button[data-tab*='wait'],[class*='tab']:nth-child(2)");
    private final By prescriptionTab  = By.cssSelector("a[href*='prescription'],button[data-tab*='prescription'],[class*='tab']:nth-child(3)");
    private final By invoiceTab       = By.cssSelector("a[href*='invoice'],button[data-tab*='invoice'],[class*='tab']:nth-child(4)");

    private final By allTabs          = By.cssSelector("[class*='tab-item'],[class*='nav-item'],[role='tab'],.nav-link");

    private final By noAppointmentMsg = By.cssSelector("[class*='empty'],[class*='no-data'],[class*='no-appointment']");
    private final By appointmentCards = By.cssSelector("[class*='appointment-card'],[class*='booking-item']");

    private final By changePasswordBtn = By.cssSelector("button[class*='btn-outline'],button[class*='change-pass'],[class*='change-password']");
    private final By currentPassField  = By.cssSelector("input[name='currentPassword'],input[type='password']:nth-of-type(1)");
    private final By newPassField      = By.cssSelector("input[name='newPassword'],input[type='password']:nth-of-type(2)");
    private final By confirmPassField  = By.cssSelector("input[name='confirmPassword'],input[type='password']:nth-of-type(3)");
    private final By updatePassBtn     = By.cssSelector("button[type='submit'],[class*='update'],[class*='save']");
    private final By passErrorMsg      = By.cssSelector(".alert-danger,.text-danger,.invalid-feedback");

    private final By headerDropdownBtn = By.cssSelector("div.dropdown.user-dropdown button.btn-link");
    private final By logoutMenuItem    = By.cssSelector("a[href*='logout'],[class*='logout'],[onclick*='logout']");
    private final By logoutByText      = By.xpath("//*[normalize-space(text())='\u0110\u0103ng xu\u1ea5t' or normalize-space(text())='Logout']");

    public ProfilePage open() {
        navigateTo("/user-info");
        log.info("Mo trang Ho so nguoi dung");
        try { Thread.sleep(1500); } catch (Exception ignored) {}
        return this;
    }

    public void clickTab(String tabName) {
        int index;
        switch (tabName.toLowerCase()) {
            case "lich hen":
            case "l\u1ecbch h\u1eb9n":      index = 0; break;
            case "danh sach cho":
            case "danh s\u00e1ch ch\u1edd": index = 1; break;
            case "don thuoc":
            case "\u0111\u01a1n thu\u1ed1c": index = 2; break;
            case "hoa don":
            case "h\u00f3a \u0111\u01a1n":  index = 3; break;
            default: index = 0;
        }
        try {
            List<WebElement> tabs = driver.findElements(allTabs);
            if (index < tabs.size()) {
                jsClick(tabs.get(index));
                log.info("Click tab index {}", index);
            }
        } catch (Exception e) {
            log.warn("Khong tim thay tab: {}", tabName);
        }
    }

    public void clickChangePassword() {
        try { jsClick(changePasswordBtn); } catch (Exception e) {
            try {
                WebElement btn = driver.findElements(By.cssSelector("button"))
                    .stream().filter(b -> b.getText().contains("i m"))
                    .findFirst().orElseThrow();
                jsClick(btn);
            } catch (Exception ex) { log.warn("Khong tim thay nut doi mat khau"); }
        }
    }

    public void changePassword(String current, String newPass, String confirm) {
        try {
            type(currentPassField, current);
            type(newPassField, newPass);
            type(confirmPassField, confirm);
            jsClick(updatePassBtn);
        } catch (Exception e) { log.warn("Loi doi mat khau: {}", e.getMessage()); }
    }

    public void logout() {
        dismissToast();
        try { Thread.sleep(500); } catch (Exception ignored) {}

        try {
            WebElement btn = driver.findElement(headerDropdownBtn);
            jsClick(btn);
            log.info("Click header dropdown");
        } catch (Exception e) {
            try {
                WebElement btn = driver.findElement(
                    By.xpath("//header//button[contains(@class,'btn-link')]"));
                jsClick(btn);
            } catch (Exception ex) {
                throw new RuntimeException("Khong tim thay dropdown button: " + ex.getMessage());
            }
        }

        try { Thread.sleep(600); } catch (Exception ignored) {}

        try {
            WebElement logoutBtn = driver.findElement(logoutByText);
            jsClick(logoutBtn);
            log.info("Click Dang xuat");
        } catch (Exception e) {
            try {
                jsClick(logoutMenuItem);
            } catch (Exception ex) {
                throw new RuntimeException("Khong tim thay nut Dang xuat: " + ex.getMessage());
            }
        }
        try { Thread.sleep(1000); } catch (Exception ignored) {}
    }

    private void dismissToast() {
        try {
            ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('.toast,.toast-container,.Toastify').forEach(e=>e.remove())");
            Thread.sleep(300);
        } catch (Exception ignored) {}
    }

    public boolean isAvatarDisplayed()           { return isDisplayed(avatar); }
    public boolean isUserNameDisplayed()          { return isDisplayed(userName); }
    public boolean isUserEmailDisplayed()         { return isDisplayed(userEmail); }
    public boolean isPhoneLabelDisplayed()        { return isDisplayed(phoneLabel); }
    public boolean isAddressLabelDisplayed()      { return isDisplayed(addressLabel); }
    public boolean isJoinDateLabelDisplayed()     { return isDisplayed(joinDateLabel); }

    public boolean isAppointmentsTabDisplayed()   {
        return driver.findElements(allTabs).size() >= 1;
    }
    public boolean isWaitlistTabDisplayed()       {
        return driver.findElements(allTabs).size() >= 2;
    }
    public boolean isPrescriptionTabDisplayed()   {
        return driver.findElements(allTabs).size() >= 3;
    }
    public boolean isInvoiceTabDisplayed()        {
        return driver.findElements(allTabs).size() >= 4;
    }

    public boolean isNoAppointmentMsgDisplayed()  { return isDisplayed(noAppointmentMsg); }
    public boolean isChangePasswordBtnDisplayed() { return isDisplayed(changePasswordBtn); }
    public boolean isChangePasswordFormDisplayed(){ return isDisplayed(currentPassField); }
    public boolean isPasswordErrorDisplayed()     { return isDisplayed(passErrorMsg); }

    public boolean isProfilePageLoaded() {
        return driver.findElements(allTabs).size() > 0 || isDisplayed(avatar);
    }

    public String getUserNameText()  { try { return getText(userName); } catch (Exception e) { return ""; } }
    public String getUserEmailText() { try { return getText(userEmail); } catch (Exception e) { return ""; } }
    public String getCurrentUrl()    { return driver.getCurrentUrl(); }
    public int getAppointmentCount() { return driver.findElements(appointmentCards).size(); }

    public boolean isAppointmentsTabActiveByDefault() {
        try {
            List<WebElement> tabs = driver.findElements(allTabs);
            if (!tabs.isEmpty()) {
                String cls = tabs.get(0).getAttribute("class");
                return cls != null && (cls.contains("active") || cls.contains("selected"));
            }
        } catch (Exception ignored) {}
        return false;
    }

}

