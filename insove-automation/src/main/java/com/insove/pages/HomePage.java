package com.insove.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import java.util.List;

public class HomePage extends BasePage {

    private final By logo          = By.cssSelector("header img, .header img, nav img, [class*='logo'] img, [class*='brand'] img");
    private final By navMenu       = By.cssSelector("header nav, header ul, .navbar, nav");
    private final By menuItems     = By.cssSelector("nav a, header nav a, .navbar a, .nav-link");

    private final By heroTitle     = By.cssSelector(".hero h1, .hero h2, .banner h1, section h1, [class*='hero'] h1, [class*='banner'] h1");
    private final By heroContactBtn = By.cssSelector(".hero a[href], .hero button, [class*='hero'] a, [class*='banner'] a");
    private final By heroBanner    = By.cssSelector("[class*='hero'], [class*='banner'], section:first-of-type");

    private final By statsSection  = By.cssSelector("[class*='stat'], [class*='counter'], [class*='number']");

    private final By serviceCards  = By.cssSelector("[class*='service-card'], [class*='service-item'], [class*='service'] .card");

    private final By faqSection    = By.cssSelector("[class*='faq'], [class*='accordion'], .accordion");
    private final By faqItems      = By.cssSelector("[class*='faq'] button, .accordion-button, [class*='accordion'] button, details");

    private final By articlesSection = By.cssSelector("[class*='blog'], [class*='post'], [class*='article'], [class*='news']");
    private final By articleCards  = By.cssSelector("[class*='post-card'], [class*='blog-card'], [class*='article-card'], .card[class*='post'], .card[class*='blog']");
    private final By readMoreAllBtn = By.cssSelector("[class*='view-all'], [class*='read-more-all'], [href*='blog'], [href*='post']");

    private final By footerEl      = By.cssSelector("footer, #footer, [class*='footer']");
    private final By footerCopyright = By.cssSelector("footer p, footer span, #footer p, [class*='footer'] p, [class*='copyright']");
    private final By footerContact  = By.cssSelector("footer [class*='contact'], footer address, [class*='footer-contact']");
    private final By footerSocial   = By.cssSelector("footer a[href*='facebook'], footer a[href*='twitter'], footer a[href*='instagram'], footer a[href*='social']");

    private final By subscribeSection = By.cssSelector("[class*='subscribe'], [class*='newsletter'], form[class*='sub']");
    private final By subscribeEmail   = By.cssSelector("[class*='subscribe'] input[type='email'], [class*='newsletter'] input, form[class*='sub'] input");
    private final By subscribeBtn     = By.cssSelector("[class*='subscribe'] button, [class*='newsletter'] button");

    public HomePage open() {
        navigateTo("/");
        log.info("Mo trang chu");
        try { Thread.sleep(1500); } catch (Exception ignored) {}
        return this;
    }

    public void clickContactButton() {
        try { jsClick(heroContactBtn); } catch (Exception ignored) {}
    }

    public void clickFaqItem(int index) {
        try {
            List<WebElement> items = driver.findElements(faqItems);
            if (index < items.size()) jsClick(items.get(index));
        } catch (Exception ignored) {}
    }

    public void scrollToFooter() {
        try {
            WebElement footer = driver.findElement(footerEl);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true)", footer);
            Thread.sleep(500);
        } catch (Exception ignored) {}
    }

    public void submitSubscribeEmail(String email) {
        try {
            type(subscribeEmail, email);
            jsClick(subscribeBtn);
        } catch (Exception ignored) {}
    }

    public boolean isLogoDisplayed() {
        String[] selectors = {
            "header img", ".header img", "a img[alt*='logo']", "a img[alt*='Logo']",
            "[class*='logo'] img", "[class*='brand'] img", "nav img", ".navbar-brand img"
        };
        for (String sel : selectors) {
            try {
                if (driver.findElement(By.cssSelector(sel)).isDisplayed()) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isNavMenuDisplayed()        { return isDisplayed(navMenu); }

    public boolean isHeroTitleDisplayed() {
        String[] selectors = { ".hero h1", ".hero h2", "[class*='hero'] h1",
            "[class*='banner'] h1", "section h1", "main h1", "h1" };
        for (String sel : selectors) {
            try {
                if (driver.findElement(By.cssSelector(sel)).isDisplayed()) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isHeroContactBtnDisplayed() {
        String[] selectors = { ".hero a", "[class*='hero'] a[class*='btn']",
            "[class*='banner'] a", "section:first-of-type a[class*='btn']" };
        for (String sel : selectors) {
            try {
                if (driver.findElement(By.cssSelector(sel)).isDisplayed()) return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    public boolean isStatsDisplayed()        { return isDisplayed(statsSection); }
    public boolean isFaqSectionDisplayed()   { return isDisplayed(faqSection); }
    public boolean isArticlesTitleDisplayed(){ return isDisplayed(articlesSection); }
    public boolean isSubscribeFormDisplayed(){ return isDisplayed(subscribeSection); }

    public boolean isCopyrightDisplayed() {
        try { scrollToFooter(); } catch (Exception ignored) {}
        return isDisplayed(footerCopyright) || isDisplayed(footerEl);
    }

    public boolean isStickyHeader() {
        try {
            String cls = driver.findElement(By.cssSelector("header")).getAttribute("class");
            return cls != null && (cls.contains("sticky") || cls.contains("fixed"));
        } catch (Exception e) { return false; }
    }

    public int getMenuItemsCount()    { return driver.findElements(menuItems).size(); }
    public int getArticleCardsCount() { return driver.findElements(articleCards).size(); }
    public int getServiceCardsCount() { return driver.findElements(serviceCards).size(); }
    public int getFaqItemsCount()     { return driver.findElements(faqItems).size(); }

    public String getHeroTitleText() {
        try {
            String[] selectors = { ".hero h1", "[class*='hero'] h1", "section h1", "main h1", "h1" };
            for (String sel : selectors) {
                try {
                    WebElement el = driver.findElement(By.cssSelector(sel));
                    if (el.isDisplayed()) return el.getText();
                } catch (Exception ignored) {}
            }
        } catch (Exception ignored) {}
        return "";
    }
}
