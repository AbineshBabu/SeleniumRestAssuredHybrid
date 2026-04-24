package com.framework.constants;

/**
 * FrameworkConstants
 * Central registry for all framework-level constants.
 *
 * ENHANCEMENT: Added microservice endpoint constants and schema paths.
 */
public final class FrameworkConstants {

    private FrameworkConstants() {
        throw new UnsupportedOperationException("Utility class - do not instantiate");
    }

    // ── Timeouts (seconds) ────────────────────────────────────────────────────
    public static final int EXPLICIT_WAIT           = 15;
    public static final int IMPLICIT_WAIT           = 5;
    public static final int PAGE_LOAD_TIMEOUT       = 30;
    public static final int FLUENT_WAIT_MAX         = 20;
    public static final int FLUENT_WAIT_POLLING     = 2;

    // ── HTTP Status Codes ─────────────────────────────────────────────────────
    public static final int HTTP_OK                 = 200;
    public static final int HTTP_CREATED            = 201;
    public static final int HTTP_NO_CONTENT         = 204;
    public static final int HTTP_BAD_REQUEST        = 400;
    public static final int HTTP_UNAUTHORIZED       = 401;
    public static final int HTTP_NOT_FOUND          = 404;

    // ── Microservice Endpoints ────────────────────────────────────────────────
    public static final String USERS_ENDPOINT       = "/users";
    public static final String POSTS_ENDPOINT       = "/posts";
    public static final String COMMENTS_ENDPOINT    = "/comments";
    public static final String ALBUMS_ENDPOINT      = "/albums";
    public static final String TODOS_ENDPOINT       = "/todos";

    // ── Browser Names ─────────────────────────────────────────────────────────
    public static final String CHROME               = "chrome";
    public static final String FIREFOX              = "firefox";
    public static final String EDGE                 = "edge";

    // ── Paths ─────────────────────────────────────────────────────────────────
    public static final String SCREENSHOT_DIR       = "test-output/screenshots/";
    public static final String EXTENT_REPORT_PATH   = "test-output/reports/ExtentReport.html";
    public static final String SCHEMA_BASE_PATH     = "schemas/";

    // ── UI Test Data ──────────────────────────────────────────────────────────
    public static final String VALID_USERNAME       = "student";
    public static final String VALID_PASSWORD       = "Password123";
}
