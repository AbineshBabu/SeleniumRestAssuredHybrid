package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ScenarioContext
 * Thread-safe key-value store shared between Cucumber step definition classes.
 * Injected via PicoContainer DI — each scenario gets a fresh instance.
 *
 * ENHANCEMENT: Added microservice-related keys (ACTIVE_SERVICE, SERVICE_BASE_URI,
 * USER_ID, POST_ID, COMMENT_ID, etc.) for cross-service data sharing.
 */
public class ScenarioContext {

    private static final Logger log = LogManager.getLogger(ScenarioContext.class);
    private final Map<String, Object> context = new HashMap<>();

    public enum Key {
        // ── API Core ──────────────────────────────────────────────────────────
        RESPONSE,           // raw REST Assured Response — used for schema validation
        RESPONSE_BODY,      // deserialised typed response DTO (PostResponse, UserResponse, etc.)
        STATUS_CODE, RESOURCE_ID, REQUEST_BODY,
        REQUEST_SPEC,

        // ── Microservice Context (NEW) ────────────────────────────────────────
        ACTIVE_SERVICE, SERVICE_BASE_URI,

        // ── Cross-Service Resource IDs (NEW) ──────────────────────────────────
        USER_ID, POST_ID, COMMENT_ID, ALBUM_ID, TODO_ID,

        // ── UI Core ───────────────────────────────────────────────────────────
        LOGIN_PAGE, DASHBOARD_PAGE, PAGE_TITLE, TEST_DATA, ERROR_MESSAGE, HOME_PAGE, FORM_PAGE,
        WEBTABLE_PAGE, IFRAME_PAGE, SHADOWDOM_PAGE, ALERT_PAGE,MODALPOP_PAGE
    }

    public void set(Key key, Object value) {
        log.debug("Context SET [{}] = {}", key, value);
        context.put(key.name(), value);
    }

    public void set(String key, Object value) {
        context.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Key key) {
        Object value = context.get(key.name());
        if (value == null) {
            throw new IllegalArgumentException(
                "ScenarioContext: No value for key [" + key + "]. Check preceding step sets it.");
        }
        return (T) value;
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getOptional(Key key) {
        return Optional.ofNullable((T) context.get(key.name()));
    }

    public boolean contains(Key key) { return context.containsKey(key.name()); }
    public void remove(Key key)      { context.remove(key.name()); }

    public void clear() {
        log.debug("Clearing ScenarioContext ({} keys)", context.size());
        context.clear();
    }
}
