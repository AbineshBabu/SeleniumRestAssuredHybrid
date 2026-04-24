package com.framework.driver;

import com.framework.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * DriverManager
 * Thread-safe WebDriver lifecycle management using ThreadLocal.
 *
 * FIX APPLIED: Full browser switching (Chrome / Firefox / Edge),
 * headless mode via Owner config, and Selenium Grid remote execution.
 *
 * Previous Issue:
 *   initDriver() ignored the browserName parameter and always created Chrome.
 *   isHeadless() read System.getProperty() directly, bypassing Owner config.
 *   createRemoteDriver() existed but was never called.
 *
 * Now:
 *   - switch block routes to the correct browser factory method.
 *   - headless mode reads from config.headless() (Owner), which merges
 *     system properties, env vars, and config.properties automatically.
 *   - Remote execution is checked first; if browser.remote=true, the
 *     browser options are sent to the Grid URL instead of creating locally.
 */
public final class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ConfigManager config = ConfigManager.ConfigProvider.getInstance();
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}


    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initDriver(config.browser());
        }
        return driverThreadLocal.get();
    }

    public static void initDriver(String browserName) {
        String browser = browserName.trim().toLowerCase();
        log.info("Initialising browser [{}] | Headless: {} | Remote: {} | Thread: {}",
                browser, config.headless(), config.remoteExecution(), Thread.currentThread().getId());

        WebDriver driver;

        if (config.remoteExecution()) {
            driver = createRemoteDriver(browser);
        } else {
            driver = createLocalDriver(browser);
        }

        configureTimeouts(driver);
        driverThreadLocal.set(driver);
        log.info("WebDriver ready — browser [{}] on thread [{}]", browser, Thread.currentThread().getId());
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver on thread [{}]", Thread.currentThread().getId());
            driver.quit();
            driverThreadLocal.remove();
        }
    }


    private static WebDriver createLocalDriver(String browser) {
        switch (browser) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            default:
                log.warn("Unknown browser '{}' — defaulting to Chrome", browser);
                return createChromeDriver();
        }
    }


    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        if (config.headless()) {
            options.addArguments("--headless=new");
            log.info("Chrome: HEADLESS mode");
        } else {
            log.info("Chrome: VISIBLE mode");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-logging"});

        return new ChromeDriver(options);
    }

    // ── Firefox (FIX: headless support added) ─────────────────────────────────

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1920", "--height=1080");

        if (config.headless()) {
            options.addArguments("-headless");
            log.info("Firefox: HEADLESS mode");
        } else {
            log.info("Firefox: VISIBLE mode");
        }

        return new FirefoxDriver(options);
    }

    // ── Edge (FIX: headless support added) ────────────────────────────────────

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");

        if (config.headless()) {
            options.addArguments("--headless=new");
            log.info("Edge: HEADLESS mode");
        } else {
            log.info("Edge: VISIBLE mode");
        }

        return new EdgeDriver(options);
    }

    // ── Remote / Grid (FIX: now actually invoked when browser.remote=true) ────

    private static WebDriver createRemoteDriver(String browser) {
        log.info("Creating REMOTE driver for [{}] on Grid: {}", browser, config.gridUrl());

        AbstractDriverOptions<?> options;
        switch (browser) {
            case "firefox":
                FirefoxOptions ffOpts = new FirefoxOptions();
                if (config.headless()) ffOpts.addArguments("-headless");
                options = ffOpts;
                break;
            case "edge":
                EdgeOptions edgeOpts = new EdgeOptions();
                if (config.headless()) edgeOpts.addArguments("--headless=new");
                options = edgeOpts;
                break;
            case "chrome":
            default:
                ChromeOptions chromeOpts = new ChromeOptions();
                if (config.headless()) chromeOpts.addArguments("--headless=new");
                chromeOpts.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                options = chromeOpts;
                break;
        }

        try {
            return new RemoteWebDriver(new URL(config.gridUrl()), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Grid URL: " + config.gridUrl(), e);
        }
    }

    // ── Timeout Configuration ─────────────────────────────────────────────────

    private static void configureTimeouts(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.implicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.pageLoadTimeout()));
        log.debug("Timeouts set — implicit: {}s, pageLoad: {}s", config.implicitWait(), config.pageLoadTimeout());
    }
}
