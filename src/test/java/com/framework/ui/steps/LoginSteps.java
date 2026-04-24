//package com.framework.ui.steps;
//
//import com.framework.config.ConfigManager;
//import com.framework.pages.DashboardPage;
//import com.framework.pages.LoginPage;
//import com.framework.utils.ScenarioContext;
//import com.framework.utils.ScenarioContext.Key;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.assertj.core.api.SoftAssertions;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
///**
// * LoginSteps
// * Cucumber step definitions for UI login scenarios.
// *
// * ENHANCEMENT: Added negative test step definitions:
// *   - "the user should see an error message"
// *   - "the error message should contain {string}"
// *   - "the user should remain on the login page"
// * These support the new Scenario Outline in login.feature for
// * data-driven testing with invalid credentials.
// *
// * Interview Tip:
// *   The Scenario Outline Examples table drives multiple executions
// *   through the same steps. Each row becomes an independent test.
// *   The negative steps reuse the existing LoginPage.getErrorMessage()
// *   and LoginPage.isErrorDisplayed() methods — the Page Object already
// *   had the capability, it just wasn't exercised.
// */
//public class LoginSteps {
//
//    private static final Logger log = LogManager.getLogger(LoginSteps.class);
//
//    private final ScenarioContext context;
//    private final ConfigManager   config;
//    private LoginPage             loginPage;
//    private DashboardPage         dashboardPage;
//
//    public LoginSteps(ScenarioContext context) {
//        this.context = context;
//        this.config  = ConfigManager.ConfigProvider.getInstance();
//    }
//
//    // ── Given ─────────────────────────────────────────────────────────────────
//
//    @Given("the user is on the login page")
//    public void theUserIsOnTheLoginPage() {
//        log.info("STEP: Opening login page");
//        loginPage = new LoginPage();
//        loginPage.open(config.uiBaseUrl());
//
//        assertThat(loginPage.isLoaded())
//            .as("Login page should be loaded — login button should be visible")
//            .isTrue();
//
//        context.set(Key.LOGIN_PAGE, loginPage);
//    }
//
//    // ── When ──────────────────────────────────────────────────────────────────
//
//    @When("the user enters username {string} and password {string}")
//    public void theUserEntersCredentials(String username, String password) {
//        log.info("STEP: Entering credentials — Username: {}", username);
//        loginPage = context.get(Key.LOGIN_PAGE);
//        loginPage.enterUsername(username).enterPassword(password);
//    }
//
//    @When("the user clicks the login button")
//    public void theUserClicksLoginButton() {
//        log.info("STEP: Clicking login button");
//        loginPage = context.get(Key.LOGIN_PAGE);
//
//        // For negative tests, clickLogin() still returns DashboardPage object,
//        // but isLoaded() will be false. We store the loginPage in context too
//        // so negative assertion steps can access it.
//        dashboardPage = loginPage.clickLogin();
//        context.set(Key.DASHBOARD_PAGE, dashboardPage);
//        context.set(Key.LOGIN_PAGE, loginPage);
//    }
//
//    // ── Then (Positive) ───────────────────────────────────────────────────────
//
//    @Then("the user should be redirected to the dashboard page")
//    public void theUserShouldBeRedirectedToDashboard() {
//        log.info("STEP: Verifying dashboard page loaded");
//        dashboardPage = context.get(Key.DASHBOARD_PAGE);
//
//        assertThat(dashboardPage.isLoaded())
//            .as("Dashboard page should be loaded after successful login")
//            .isTrue();
//    }
//
//    @And("the page heading should contain {string}")
//    public void thePageHeadingShouldContain(String expectedText) {
//        log.info("STEP: Verifying page heading contains '{}'", expectedText);
//        dashboardPage = context.get(Key.DASHBOARD_PAGE);
//
//        String actualHeading = dashboardPage.getPageHeading();
//        log.info("Actual heading: '{}'", actualHeading);
//
//        assertThat(actualHeading)
//            .as("Page heading should contain expected success text")
//            .containsIgnoringCase(expectedText);
//    }
//
//    @And("the logout button should be visible")
//    public void theLogoutButtonShouldBeVisible() {
//        log.info("STEP: Verifying logout button and success banner visible");
//        dashboardPage = context.get(Key.DASHBOARD_PAGE);
//
//        SoftAssertions softly = new SoftAssertions();
//        softly.assertThat(dashboardPage.isLogoutVisible())
//              .as("Logout link should be visible after successful login")
//              .isTrue();
//        softly.assertThat(dashboardPage.isSuccessBannerDisplayed())
//              .as("Success banner should be displayed on dashboard")
//              .isTrue();
//        softly.assertAll();
//    }
//
//    @And("the current URL should contain {string}")
//    public void theCurrentUrlShouldContain(String partialUrl) {
//        dashboardPage = context.get(Key.DASHBOARD_PAGE);
//        assertThat(dashboardPage.getCurrentUrl())
//                .as("URL should contain '%s' after successful login", partialUrl)
//                .contains(partialUrl);
//    }
//
//    // ── Then (Negative — NEW for data-driven testing) ─────────────────────────
//
//    /**
//     * Verifies that an error message is displayed after failed login.
//     * Uses LoginPage.isErrorDisplayed() which was already implemented
//     * in the Page Object but never tested until now.
//     */
//    @Then("the user should see an error message")
//    public void theUserShouldSeeAnErrorMessage() {
//        log.info("STEP: Verifying error message is displayed");
//        loginPage = context.get(Key.LOGIN_PAGE);
//
//        assertThat(loginPage.isErrorDisplayed())
//            .as("Error message should be visible after invalid login attempt")
//            .isTrue();
//    }
//
//    /**
//     * Verifies the error message text matches the expected message
//     * from the Scenario Outline Examples table.
//     */
//    @And("the error message should contain {string}")
//    public void theErrorMessageShouldContain(String expectedError) {
//        log.info("STEP: Verifying error message contains '{}'", expectedError);
//        loginPage = context.get(Key.LOGIN_PAGE);
//
//        String actualError = loginPage.getErrorMessage();
//        log.info("Actual error: '{}'", actualError);
//
//        assertThat(actualError)
//            .as("Error message should contain expected text")
//            .containsIgnoringCase(expectedError);
//    }
//
//    /**
//     * Verifies the user was NOT redirected away from the login page.
//     * Checks that the URL still contains the login page path.
//     */
//    @And("the user should remain on the login page")
//    public void theUserShouldRemainOnTheLoginPage() {
//        log.info("STEP: Verifying user is still on login page");
//        loginPage = context.get(Key.LOGIN_PAGE);
//
//        String currentUrl = loginPage.getCurrentUrl();
//        log.info("Current URL: {}", currentUrl);
//
//        assertThat(currentUrl)
//            .as("User should remain on the login page after invalid credentials")
//            .contains("practice-test-login");
//    }
//}
