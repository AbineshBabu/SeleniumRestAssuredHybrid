package com.framework.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;

/**
 * TestRunner — Single Generic Runner (UI + API + Parallel)
 *
 * This is the ONLY runner in the framework.
 * APITestRunner and UITestRunner have been removed — they are not needed.
 *
 * ─── WHY ONE RUNNER IS ENOUGH ────────────────────────────────────────────────
 *
 *  1. Scans ALL feature files under features/ (both ui/ and api/ subfolders)
 *  2. Loads ALL step definitions and hooks in one glue scan
 *  3. Tag-aware hooks prevent any cross-contamination between UI and API tests:
 *       UIHooks  @Before("@ui")  → browser is created ONLY for @ui scenarios
 *       APIHooks @Before("@api") → REST Assured setup ONLY for @api scenarios
 *  4. No tags hardcoded here — filtering is 100% CLI-driven at runtime
 *
 * ─── WHY ONE RUNNER IS ENOUGH FOR PARALLEL ───────────────────────────────────
 *
 *  Thread-safety comes from the FRAMEWORK INFRASTRUCTURE, not from having
 *  multiple runner classes:
 *
 *    DriverManager   → ThreadLocal<WebDriver>
 *                      Each thread gets its own isolated browser instance.
 *                      Threads never share a WebDriver object.
 *
 *    ScenarioContext → Injected fresh per scenario via PicoContainer DI.
 *                      Each scenario has completely isolated state.
 *                      No static fields, no shared maps.
 *
 *    APISteps        → REST Assured is stateless by design.
 *                      Completely safe to run across any number of threads.
 *
 *  A second runner class adds zero thread-safety — it just duplicates code.
 *
 * ─── PARALLEL CONFIGURATION ────────────────────────────────────────────────
 *
 *  @DataProvider(parallel = true)       → TestNG runs scenarios concurrently
 *  data-provider-thread-count (testng.xml) → controls number of threads
 *
 *  Recommended thread counts:
 *    @api only  → 6–8  (no browser overhead, very fast)
 *    @ui only   → 2–3  (each thread spawns a full browser, RAM-dependent)
 *    mixed run  → 3–4  (safe default on most developer machines)
 *
 * ─── EXECUTION COMMANDS ──────────────────────────────────────────────────────
 *
 *  mvn clean test                                           → all tests (parallel)
 *  mvn clean test -Dcucumber.filter.tags="@ui"              → UI only
 *  mvn clean test -Dcucumber.filter.tags="@api"             → API only
 *  mvn clean test -Dcucumber.filter.tags="@smoke"           → smoke suite
 *  mvn clean test -Dcucumber.filter.tags="@regression"      → regression suite
 *  mvn clean test -Dcucumber.filter.tags="@ui and @smoke"   → UI smoke only
 *  mvn clean test -Dcucumber.filter.tags="@api and @smoke"  → API smoke only
 *  mvn clean test -Dcucumber.filter.tags="@user-service"    → one microservice
 *  ede70e7752fb6c36ebd2d964e49a7240
 *  ATTA8294f71745c5112d8092f0a5e5dfef0aefd3f41489a12a339015232f61d1d3ce2E56A35A
 */
@CucumberOptions(
        features  = "src/test/resources/features",  // scans features/ui AND features/api
        glue      = {
                "com.framework.ui.steps",            // UI step definitions (LoginSteps etc.)
                "com.framework.api.steps",           // API step definitions (APISteps etc.)
                "com.framework.hooks"                // UIHooks (@ui) + APIHooks (@api)
        },
        // No tags here — pass -Dcucumber.filter.tags="@ui/@api/@smoke" on CLI
        plugin    = {
                "pretty",
                "html:test-output/reports/cucumber-report.html",
                "json:test-output/reports/cucumber-report.json",
                "junit:test-output/reports/cucumber-report.xml",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "timeline:test-output/reports/timeline"
        },
        monochrome = true,
        dryRun     = false
)
public class TestRunner extends AbstractTestNGCucumberTests {

    private static final Logger log = LogManager.getLogger(TestRunner.class);

    /**
     * parallel = true  → TestNG distributes all Cucumber scenarios across
     *                    a thread pool. Pool size = data-provider-thread-count
     *                    in testng.xml.
     *
     * Thread-safety is guaranteed by:
     *   - DriverManager.ThreadLocal<WebDriver>  (one browser per thread)
     *   - ScenarioContext via PicoContainer DI  (one context per scenario)
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        log.info("╔═══════════════════════════════════════════════╗");
        log.info("║          HYBRID TEST FRAMEWORK                ║");
        log.info("║          Single Generic Runner                ║");
        log.info("╠═══════════════════════════════════════════════╣");
        log.info("║  Tag Filter : {}", System.getProperty(
                "cucumber.filter.tags", "NONE — running ALL tests"));
        log.info("║  Parallel   : ENABLED                         ║");
        log.info("║  Features   : features/ui + features/api      ║");
        log.info("╚═══════════════════════════════════════════════╝");
        return super.scenarios();
    }
}
