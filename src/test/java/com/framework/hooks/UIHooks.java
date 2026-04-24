package com.framework.hooks;

import com.framework.config.ConfigManager;
import com.framework.driver.DriverManager;
import com.framework.utils.ScenarioContext;
import com.framework.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * UIHooks
 * Cucumber lifecycle hooks for UI scenarios only (tagged @ui).
 *
 * Interview Tip:
 * - order on @Before/@After controls execution sequence when multiple hooks exist.
 * - Screenshots captured via scenario.attach() embed directly into HTML report.
 * - Always quit driver in @After even if test fails — prevents orphaned browsers.
 * - @BeforeAll/@AfterAll run once per Cucumber suite, not per scenario.
 */
public class UIHooks {

    private static final Logger log = LogManager.getLogger(UIHooks.class);
    private static final ConfigManager config = ConfigManager.ConfigProvider.getInstance();

    private final ScenarioContext context;

    public UIHooks(ScenarioContext context) {
        this.context = context;
    }

    @BeforeAll
    public static void beforeSuite() {
        log.info("========================================");
        log.info("       UI TEST SUITE STARTING          ");
        log.info("  Browser  : {}", ConfigManager.ConfigProvider.getInstance().browser());
        log.info("  Base URL : {}", ConfigManager.ConfigProvider.getInstance().uiBaseUrl());
        log.info("  Env      : {}", ConfigManager.ConfigProvider.getInstance().environment());
        log.info("========================================");
    }

    @AfterAll
    public static void afterSuite() {
        log.info("========================================");
        log.info("       UI TEST SUITE COMPLETED         ");
        log.info("========================================");
    }

    @Before(value = "@ui", order = 1)
    public void beforeUIScenario(Scenario scenario) {
        log.info(">> START [UI] Scenario: {}", scenario.getName());
        log.info("   Tags: {}", scenario.getSourceTagNames());
        DriverManager.initDriver(config.browser());
    }

    @After(value = "@ui", order = 1)
    public void afterUIScenario(Scenario scenario) {
        try {
            if (scenario.isFailed() && config.screenshotOnFailure()) {
                log.warn("Scenario FAILED — capturing screenshot");
                ScreenshotUtils.captureAndEmbedScreenshot(scenario);
            } else if (!scenario.isFailed() && config.screenshotOnPass()) {
                ScreenshotUtils.captureAndEmbedScreenshot(scenario);
            }
        } finally {
            DriverManager.quitDriver();
            context.clear();
            String status = scenario.isFailed() ? "FAILED" : "PASSED";
            log.info("<< END [UI] [{}] Scenario: {}", status, scenario.getName());
        }
    }
}
