package com.framework.hooks;

import com.framework.config.ConfigManager;
import com.framework.utils.APIRequestBuilder;
import com.framework.utils.ScenarioContext;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * APIHooks
 * Cucumber lifecycle hooks for API scenarios only (tagged @api).
 *
 * Interview Tip: Separate hook classes per test type = Single Responsibility.
 * Adding GraphQL or gRPC tests means adding new hook classes, not modifying
 * existing ones — Open/Closed Principle in practice.
 */
public class APIHooks {

    private static final Logger log = LogManager.getLogger(APIHooks.class);

    private final ScenarioContext context;

    public APIHooks(ScenarioContext context) {
        this.context = context;
    }

    @BeforeAll
    public static void beforeAPISuite() {
        log.info("========================================");
        log.info("       API TEST SUITE STARTING         ");
        log.info("  Base URI : {}", ConfigManager.ConfigProvider.getInstance().apiBaseUri());
        log.info("  Env      : {}", ConfigManager.ConfigProvider.getInstance().environment());
        log.info("========================================");
        APIRequestBuilder.configureRestAssured();
    }

    @AfterAll
    public static void afterAPISuite() {
        log.info("========================================");
        log.info("       API TEST SUITE COMPLETED        ");
        log.info("========================================");
    }

    @Before(value = "@api", order = 1)
    public void beforeAPIScenario(Scenario scenario) {
        log.info(">> START [API] Scenario: {}", scenario.getName());
        log.info("   Tags: {}", scenario.getSourceTagNames());
    }

    @After(value = "@api", order = 1)
    public void afterAPIScenario(Scenario scenario) {
        context.clear();
        String status = scenario.isFailed() ? "FAILED" : "PASSED";
        log.info("<< END [API] [{}] Scenario: {}", status, scenario.getName());
        if (scenario.isFailed()) {
            log.error("   API Test Failed — review request/response logs above");
        }
    }
}
