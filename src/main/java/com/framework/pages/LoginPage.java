package com.framework.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * LoginPage
 * Page Object for: https://practicetestautomation.com/practice-test-login/
 *
 * Interview Tip: Method chaining (fluent interface) keeps step definitions
 * readable. Returning the next page from action methods makes navigation
 * explicit and catches bugs at compile time rather than runtime.
 */
public class LoginPage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────────────

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "submit")
    private WebElement loginButton;

    @FindBy(id = "error")
    private WebElement errorMessage;

    // ── Constructor ───────────────────────────────────────────────────────────

    public LoginPage() {
        super();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public LoginPage open(String url) {
        log.info("Opening Login page: {}", url);
        navigateTo(url);
        return this;
    }

    public LoginPage enterUsername(String username) {
        log.info("Entering username: {}", username);
        type(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering password: [MASKED]");
        type(passwordField, password);
        return this;
    }

    public DashboardPage clickLogin() {
        log.info("Clicking login button");
        Alert a= driver.switchTo().alert();
        driver.switchTo().frame("");
        click(loginButton);
        return new DashboardPage();
    }

    /** Convenience: full login in one call */
    public DashboardPage loginWith(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    // ── Verifications ─────────────────────────────────────────────────────────

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }

    @Override
    public boolean isLoaded() {
        boolean loaded = isDisplayed(loginButton);
        log.info("LoginPage isLoaded: {}", loaded);
        return loaded;
    }
}
