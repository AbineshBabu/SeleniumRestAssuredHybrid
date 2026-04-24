package com.framework.dto;

import com.framework.dto.response.other.*;

import java.util.HashMap;
import java.util.Map;

/**
 * DtoResponseResolver
 * Maps a service name (as registered in ServiceRegistry) to its
 * corresponding response DTO class, enabling type-safe deserialisation
 * in step definitions without a switch/if-else chain.
 *
 * Usage in APISteps:
 *   Class<?> dtoClass = DtoResponseResolver.resolve(serviceName);
 *   Object   dto      = response.as(dtoClass);
 *
 * Adding a new service:
 *   1. Create the response DTO in com.framework.dto.response
 *   2. Add one line here: REGISTRY.put("my-service", MyServiceResponse.class)
 *   That's all — the step definitions need no changes.
 *
 * Design note: This is the Registry Pattern. The Map acts as a lookup
 * table that decouples service names from concrete DTO types at
 * compile time. The resolver is the single place to update when
 * a new microservice is added to the framework.
 */
public final class DtoResponseResolver {

    private static final Map<String, Class<?>> REGISTRY = new HashMap<>();
    
    static {
        REGISTRY.put("post",    PostResponse.class);
        REGISTRY.put("user",    UserResponse.class);
        REGISTRY.put("comment", CommentResponse.class);
        REGISTRY.put("album",   AlbumResponse.class);
        REGISTRY.put("todo",    TodoResponse.class);
    }

    private DtoResponseResolver() {}

    /**
     * Returns the response DTO class for the given service name.
     *
     * @param serviceName the active service name from ScenarioContext
     * @return the DTO Class object; never null
     * @throws IllegalArgumentException if the service has no registered DTO
     */
    public static Class<?> resolve(String serviceName) {
        String key = serviceName.trim().toLowerCase();
        Class<?> dtoClass = REGISTRY.get(key);
        if (dtoClass == null) {
            throw new IllegalArgumentException(
                "No response DTO registered for service: '" + serviceName
                + "'. Register it in DtoResponseResolver."
            );
        }
        return dtoClass;
    }

    /**
     * Checks whether a DTO is registered for the given service name.
     * Use this in steps that only assert on specific services (e.g. schema-only).
     */
    public static boolean isRegistered(String serviceName) {
        return REGISTRY.containsKey(serviceName.trim().toLowerCase());
    }
}
