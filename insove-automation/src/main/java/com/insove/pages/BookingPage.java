package com.insove.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.List;

public class BookingPage extends BasePage {

    private final By branchSection   = By.cssSelector("form, [class*='booking'], [class*='form'], main section:first-of-type");
    private final By dateTimeSection = By.cssSelector("[class*='calendar'],[class*='date-picker'],[class*='rdp'],[class*='datepicker'],input[type='date']");
    private final By serviceSection  = By.cssSelector("[class*='service'],[class*='package'],[class*='plan']");
    private final By noteSection     = By.cssSelector("textarea,[class*='note'],[class*='additional']");
    private final By submitButton    = By.cssSelector("button[type='submit'],button[class*='btn-primary']");

    private final By branchDropdown  = By.cssSelector(
        "select[name*='branch'], [class*='branch'] select, " +
        "[class*='select'][class*='branch'], [id*='branch']");
    private final By branchOptions   = By.cssSelector(
        "select[name*='branch'] option, [class*='branch'] option, " +
        "[class*='option'][class*='branch'], [class*='select__option']");
    private final By doctorDropdown  = By.cssSelector(
        "select[name*='doctor'], [class*='doctor'] select, " +
        "[class*='select'][class*='doctor'], [id*='doctor']");
    private final By doctorOptions   = By.cssSelector(
        "select[name*='doctor'] option, [class*='doctor'] option, " +
        "[class*='option'][class*='doctor']");

    private final By calendar        = By.cssSelector("[class*='calendar'],[class*='datepicker'],[class*='rdp']");
    private final By calendarDays    = By.cssSelector("[class*='day']:not([class*='disabled']):not([class*='outside'])");
    private final By prevMonthBtn    = By.cssSelector("button[aria-label*='previous'],button[aria-label*='prev'],[class*='prev']");
    private final By nextMonthBtn    = By.cssSelector("button[aria-label*='next'],[class*='next']");
    private final By calendarTitle   = By.cssSelector("[class*='caption'],[class*='month-year'],[class*='month']");

    private final By timeSlots       = By.cssSelector("[class*='slot'] button,[class*='time'] button");
    private final By noSlotMessage   = By.cssSelector("[class*='no-slot'],[class*='empty']");

    private final By serviceCards    = By.cssSelector("[class*='package-card'],[class*='service-package'],[class*='price-card']");
    private final By serviceSelectBtns = By.cssSelector("[class*='package'] button,[class*='service'] button[class*='primary']");

    private final By noteField       = By.cssSelector("textarea");

    private final By errorMessages   = By.cssSelector(".alert-danger,.text-danger,.invalid-feedback");
    private final By successMessage  = By.cssSelector(".alert-success,[class*='success']");

    public BookingPage open() {
        navigateTo("/booking");
        log.info("Mo trang Dat lich kham");
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        return this;
    }

    public void selectBranch(String branchName) {
        try {
            click(branchDropdown);
            List<WebElement> options = driver.findElements(branchOptions);
            for (WebElement opt : options) {
                if (opt.getText().contains(branchName)) {
                    jsClick(opt);
                    log.info("Chon chi nhanh: {}", branchName);
                    return;
                }
            }
        } catch (Exception e) {
            log.warn("Khong tim thay chi nhanh: {}", branchName);
        }
    }

    public void selectDoctor(String doctorName) {
        try {
            click(doctorDropdown);
            List<WebElement> options = driver.findElements(doctorOptions);
            for (WebElement opt : options) {
                if (opt.getText().contains(doctorName)) {
                    jsClick(opt);
                    return;
                }
            }
        } catch (Exception e) {
            log.warn("Khong tim thay bac si: {}", doctorName);
        }
    }

    public void selectFirstAvailableDay() {
        try {
            List<WebElement> days = driver.findElements(calendarDays);
            if (!days.isEmpty()) { jsClick(days.get(0)); }
        } catch (Exception ignored) {}
    }

    public void selectFirstTimeSlot() {
        try {
            List<WebElement> slots = driver.findElements(timeSlots);
            if (!slots.isEmpty()) { jsClick(slots.get(0)); }
        } catch (Exception ignored) {}
    }

    public void selectServicePackage(int index) {
        try {
            List<WebElement> btns = driver.findElements(serviceSelectBtns);
            if (index < btns.size()) { jsClick(btns.get(index)); }
        } catch (Exception ignored) {}
    }

    public void enterNote(String note) {
        try { type(noteField, note); } catch (Exception ignored) {}
    }

    public void clickBookingSubmit() {
        try { jsClick(submitButton); } catch (Exception ignored) {}
    }

    public void clickNextMonth() {
        try { jsClick(nextMonthBtn); } catch (Exception ignored) {}
    }

    public boolean isBranchSectionDisplayed()   { return isDisplayed(branchSection); }
    public boolean isDateTimeSectionDisplayed()  { return isDisplayed(calendar) || isDisplayed(dateTimeSection); }
    public boolean isServiceSectionDisplayed()   { return isDisplayed(serviceSection) || driver.findElements(serviceCards).size() > 0; }
    public boolean isNoteSectionDisplayed()      { return isDisplayed(noteField); }
    public boolean isSubmitButtonDisplayed()     { return isDisplayed(submitButton); }
    public boolean isSubmitButtonEnabled()       { return isEnabled(submitButton); }
    public boolean isCalendarDisplayed()         { return isDisplayed(calendar); }
    public boolean isNoSlotMessageDisplayed()    { return isDisplayed(noSlotMessage); }
    public boolean isErrorDisplayed()            { return isDisplayed(errorMessages); }
    public boolean isSuccessDisplayed()          { return isDisplayed(successMessage); }

    public boolean isNotePlaceholderCorrect() {
        try {
            String ph = getAttribute(noteField, "placeholder");
            return ph != null && !ph.isEmpty();
        } catch (Exception e) { return false; }
    }

    public int getBranchOptionsCount() {
        try {
            jsClick(branchDropdown);
            int count = driver.findElements(branchOptions).size();
            try { ((JavascriptExecutor)driver).executeScript("document.body.click()"); } catch (Exception ignored) {}
            return count;
        } catch (Exception e) { return 0; }
    }

    public int getDoctorOptionsCount() {
        try {
            jsClick(doctorDropdown);
            int count = driver.findElements(doctorOptions).size();
            return count;
        } catch (Exception e) { return 0; }
    }

    public int getServiceCardsCount() {
        return driver.findElements(serviceCards).size();
    }

    public String getCalendarMonthYear() {
        try { return getText(calendarTitle); } catch (Exception e) { return ""; }
    }

    public String getNotePlaceholder() {
        try { return getAttribute(noteField, "placeholder"); } catch (Exception e) { return ""; }
    }
}
