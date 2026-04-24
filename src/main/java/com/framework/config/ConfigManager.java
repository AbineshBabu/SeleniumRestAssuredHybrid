package com.framework.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

/**
 * ConfigManager
 * Strongly-typed config loading via Owner library.
 *
 * ENVIRONMENT PROFILE SYSTEM:
 *   Owner supports variable interpolation in @Sources paths.
 *   The ${env} in the file path is resolved from system properties.
 *
 *   When you run: mvn clean test -Denv=staging
 *   Owner resolves: config-${env}.properties --> config-staging.properties
 *
 *   When you run: mvn clean test  (no -Denv flag)
 *   ConfigProvider sets env=qa as default system property.
 *   Owner resolves: config-${env}.properties --> config-qa.properties
 *
 * PROPERTY RESOLUTION ORDER (first match wins):
 *   1. CLI system properties     (-Dapp.ui.url=https://...)
 *   2. Environment variables     (export APP_UI_URL=https://...)
 *   3. Environment-specific file (config-qa.properties or config-staging.properties)
 *   4. Common defaults file      (config.properties -- browser, timeouts, screenshots)
 *   5. Classpath env file        (for packaged JAR runs)
 *   6. Classpath common file     (for packaged JAR runs)
 *   7. @DefaultValue annotation  (absolute last resort)
 *
 * FILE STRUCTURE:
 *   config/
 *   |- config.properties            <-- shared across ALL environments (browser, timeouts)
 *   |- config-qa.properties         <-- QA URLs, QA credentials, QA service URIs
 *   |- config-staging.properties    <-- Staging URLs, staging credentials, staging service URIs
 */
@LoadPolicy(LoadType.MERGE)
@Sources({
    "system:properties",
    "system:env",
    "file:src/test/resources/config/config-${env}.properties",
    "file:src/test/resources/config/config.properties",
    "classpath:config/config-${env}.properties",
    "classpath:config/config.properties"
})
public interface ConfigManager extends Config {

    // -- Application URLs ---------------------------------------------------

    @Key("app.ui.url")
    @DefaultValue("https://practicetestautomation.com/practice-test-login/")
    String uiBaseUrl();

    @Key("app.api.base.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String apiBaseUri();

    // -- Microservice URIs --------------------------------------------------

    @Key("api.user.service.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String userServiceUri();

    @Key("api.post.service.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String postServiceUri();

    @Key("api.comment.service.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String commentServiceUri();

    @Key("api.album.service.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String albumServiceUri();

    @Key("api.todo.service.uri")
    @DefaultValue("https://jsonplaceholder.typicode.com")
    String todoServiceUri();

    @Key("api.apirestful.service.uri")
    @DefaultValue("https://api.restful-api.dev")
    String apirestful();

    @Key("api.gorest.service.uri")
    @DefaultValue("https://gorest.co.in/public/v2")
    String gorest();

    // -- Environment --------------------------------------------------------

    @Key("env")
    @DefaultValue("qa")
    String environment();

    // -- Browser ------------------------------------------------------------

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("browser.headless")
    @DefaultValue("false")
    boolean headless();

    @Key("browser.remote")
    @DefaultValue("false")
    boolean remoteExecution();

    @Key("grid.url")
    @DefaultValue("http://localhost:4444/wd/hub")
    String gridUrl();

    // -- Timeouts -----------------------------------------------------------

    @Key("timeout.implicit")
    @DefaultValue("5")
    int implicitWait();

    @Key("timeout.explicit")
    @DefaultValue("15")
    int explicitWait();

    @Key("timeout.pageload")
    @DefaultValue("30")
    int pageLoadTimeout();

    // -- Screenshots --------------------------------------------------------

    @Key("screenshot.on.failure")
    @DefaultValue("true")
    boolean screenshotOnFailure();

    @Key("screenshot.on.pass")
    @DefaultValue("false")
    boolean screenshotOnPass();

    // -- UI Credentials -----------------------------------------------------

    @Key("ui.username")
    @DefaultValue("student")
    String uiUsername();

    @Key("ui.password")
    @DefaultValue("Password123")
    String uiPassword();

    // -- Singleton ----------------------------------------------------------

    /**
     * ConfigProvider -- Singleton accessor for ConfigManager.
     *
     * IMPORTANT: The ${env} variable in @Sources is resolved from system
     * properties BEFORE any config file is loaded. This creates a chicken-
     * and-egg problem: Owner can't read env=qa from config.properties to
     * know which file to load, because it needs the env value to find the
     * file in the first place.
     *
     * Solution: Before creating the proxy, we check if the "env" system
     * property exists. If not (user didn't pass -Denv=anything), we set
     * it to "qa" as default. This ensures ${env} always resolves, so
     * Owner can find config-qa.properties or config-staging.properties.
     *
     * When user passes -Denv=staging, that property already exists, so
     * we don't override it. ${env} resolves to "staging" and Owner loads
     * config-staging.properties.
     */
    final class ConfigProvider {

        private static final ConfigManager INSTANCE;

        static {
            if (System.getProperty("env") == null) {
                System.setProperty("env", "qa");
            }
            INSTANCE = ConfigFactory.create(
                ConfigManager.class, System.getProperties(), System.getenv()
            );
        }

        private ConfigProvider() {}

        public static ConfigManager getInstance() {
            return INSTANCE;
        }
    }
}
