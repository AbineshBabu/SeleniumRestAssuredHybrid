package com.framework.pages;

import com.framework.constants.*;
import com.framework.driver.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage
 * Abstract base class for all Page Objects.
 * Centralises Selenium interactions — no raw driver calls in step definitions.
 *
 * Interview Tip:
 * FluentWait allows custom polling interval + exception ignoring — ideal
 * for AJAX-heavy pages. ExplicitWait (WebDriverWait) is simpler for most cases.
 * @FindBy + PageFactory initialises locators lazily on first access.
 */
public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected FluentWait<WebDriver> fluentWait;
    protected Actions actions;
    protected JavascriptExecutor jsExecutor;

    protected BasePage() {
        this.driver     = DriverManager.getDriver();
        this.wait       = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        this.actions    = new Actions(driver);
        this.jsExecutor = (JavascriptExecutor) driver;
        this.fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(FrameworkConstants.FLUENT_WAIT_MAX))
                .pollingEvery(Duration.ofSeconds(FrameworkConstants.FLUENT_WAIT_POLLING))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        PageFactory.initElements(driver, this);
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String url) {
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl()  { return driver.getCurrentUrl(); }
    protected String getPageTitle()   { return driver.getTitle(); }

    // ── Interactions ──────────────────────────────────────────────────────────

    protected void click(By locator) {
        log.debug("Click: {}", locator);
        WebElement el = waitForClickable(locator);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Standard click intercepted — falling back to JS click: {}", locator);
            jsClick(el);
        }
    }

    protected void click(WebElement element) {
        waitForClickable(element).click();
    }

    protected void type(By locator, String text) {
        log.debug("Type '{}' into: {}", text, locator);
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected void type(WebElement element, String text) {
        waitForVisible(element).clear();
        element.sendKeys(text);
    }

    protected String getText(By locator)         { return waitForVisible(locator).getText().trim(); }
    protected String getText(WebElement element)  { return waitForVisible(element).getText().trim(); }

    protected boolean isDisplayed(By locator) {
        try   { return driver.findElement(locator).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }

    protected boolean isDisplayed(WebElement element) {
        
        try   { return element.isDisplayed(); }
        catch (NoSuchElementException | StaleElementReferenceException e) { return false; }
    }

    protected boolean isSelected(WebElement element){
        try {
            return element.isSelected();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {return false;}
    }

    protected List<WebElement> findElements(By locator) { return driver.findElements(locator); }

    // ── Waits ─────────────────────────────────────────────────────────────────

    protected WebElement waitForVisible(By locator)       { return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)); }
    protected WebElement waitForVisible(WebElement el)    { return wait.until(ExpectedConditions.visibilityOf(el)); }
    protected WebElement waitForClickable(By locator)     { return wait.until(ExpectedConditions.elementToBeClickable(locator)); }
    protected WebElement waitForClickable(WebElement el)  { return wait.until(ExpectedConditions.elementToBeClickable(el)); }
    protected boolean    waitForUrlContains(String url)   { return wait.until(ExpectedConditions.urlContains(url)); }

    protected <T> T fluentWaitUntil(ExpectedCondition<T> condition) { return fluentWait.until(condition); }

    // ── JavaScript Helpers ────────────────────────────────────────────────────

    protected void jsClick(WebElement element)         { jsExecutor.executeScript("arguments[0].click();", element); }
    protected void jsScrollIntoView(WebElement element) { jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element); }

    // ── Abstract Contract ─────────────────────────────────────────────────────

    /** Each page must verify its own loaded state. Called after navigation. */
    public abstract boolean isLoaded();

    public void checkCheckBox(WebElement element){
        click(element);
    }

    public void unCheckCheckBox(WebElement element){
        click(element);
    }

    public boolean isChecked(WebElement element){
        return  isSelected(element);
    }
}
