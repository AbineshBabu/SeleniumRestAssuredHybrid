package com.framework.dto.response.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserResponse DTO
 * Deserializes the response body from GET /users/{id} endpoint.
 *
 * Note: The real JSONPlaceholder /users response contains nested objects
 * (address, company). They are intentionally omitted here for simplicity;
 * add inner static classes if those nested fields need asserting.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
}
