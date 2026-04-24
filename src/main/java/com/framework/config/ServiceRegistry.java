package com.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ServiceRegistry
 * Central registry that maps microservice names to their base URIs.
 * Reads all URIs from ConfigManager at initialisation time.
 *
 * Usage in step definitions:
 *   Given the user sets up the "user" service
 *   → ServiceRegistry.getBaseUri("user") → "https://jsonplaceholder.typicode.com"
 *
 * Adding a new microservice:
 *   1. Add the @Key property in ConfigManager.
 *   2. Add the mapping in ServiceRegistry.registerServices().
 *   3. (Optional) Add a schema file in src/test/resources/schemas/.
 *   That's it — the feature file can reference it by name immediately.
 *
 * Interview Tip:
 *   This is the Strategy Pattern — the service name acts as the key
 *   to select the correct configuration at runtime. It decouples
 *   feature files from hardcoded URIs completely.
 */
public final class ServiceRegistry {

    private static final Logger log = LogManager.getLogger(ServiceRegistry.class);
    private static final ConfigManager config = ConfigManager.ConfigProvider.getInstance();
    private static final Map<String, String> SERVICE_URI_MAP = new HashMap<>();

    private ServiceRegistry() {}

    static {
        registerServices();
    }

    /**
     * Registers all known microservice name → base URI mappings.
     * Each entry reads its URI from ConfigManager, which means it
     * respects system property overrides (-Dapi.user.service.uri=...).
     */
    private static void registerServices() {
        SERVICE_URI_MAP.put("user",    config.userServiceUri());
        SERVICE_URI_MAP.put("post",    config.postServiceUri());
        SERVICE_URI_MAP.put("comment", config.commentServiceUri());
        SERVICE_URI_MAP.put("album",   config.albumServiceUri());
        SERVICE_URI_MAP.put("todo",    config.todoServiceUri());
        SERVICE_URI_MAP.put("apirestful", config.apirestful());
        SERVICE_URI_MAP.put("gorest", config.gorest());

//        log.info("ServiceRegistry initialised with {} services: {}", SERVICE_URI_MAP.size(), SERVICE_URI_MAP.keySet());
//        SERVICE_URI_MAP.forEach((name, uri) -> log.debug("  {} → {}", name, uri));
    }

    /**
     * Returns the base URI for the given service name.
     *
     * @param serviceName case-insensitive service name (e.g., "user", "post")
     * @return base URI string
     * @throws IllegalArgumentException if the service name is not registered
     */
    public static String getBaseUri(String serviceName) {
        String key = serviceName.trim().toLowerCase();
        String uri = SERVICE_URI_MAP.get(key);

        if (uri == null || uri.isBlank()) {
            throw new IllegalArgumentException(
                "Unknown service: '" + serviceName + "'. Registered services: " + SERVICE_URI_MAP.keySet()
            );
        }

        log.debug("Resolved service '{}' → '{}'", serviceName, uri);
        return uri;
    }

    /**
     * Returns all registered service names. Useful for logging and validation.
     */
    public static Set<String> getRegisteredServices() {
        return Collections.unmodifiableSet(SERVICE_URI_MAP.keySet());
    }

    /**
     * Checks whether a service name is registered.
     */
    public static boolean isRegistered(String serviceName) {
        return SERVICE_URI_MAP.containsKey(serviceName.trim().toLowerCase());
    }
}
