package com.framework.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * DashboardPage
 * Page Object for the post-login dashboard page.
 *
 * Interview Tip: Returning DashboardPage from LoginPage.clickLogin()
 * signals the expected navigation — acts as compile-time documentation.
 */
public class DashboardPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────

    @FindBy(css = "h1.post-title")
    private WebElement pageTitle;

    @FindBy(css = "a[href*='logout']")
    private WebElement logoutLink;

    @FindBy(css = "p.has-text-align-center")
    private WebElement successBanner;

    // ── Constructor ───────────────────────────────────────────────────────────

    public DashboardPage() {
        super();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public LoginPage logout() {
        log.info("Logging out");
        click(logoutLink);
        return new LoginPage();
    }

    // ── Verifications ─────────────────────────────────────────────────────────

    public String getPageHeading()           { return getText(pageTitle); }
    public String getSuccessMessage()        { return getText(successBanner); }
    public boolean isSuccessBannerDisplayed(){ return isDisplayed(successBanner); }
    public boolean isLogoutVisible()         { return isDisplayed(logoutLink); }

    @Override
    public boolean isLoaded() {
        boolean loaded = isDisplayed(pageTitle);
        log.info("DashboardPage isLoaded: {}", loaded);
        return loaded;
    }
}
