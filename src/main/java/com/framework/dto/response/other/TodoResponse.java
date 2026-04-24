package com.framework.dto.response.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TodoResponse DTO
 * Deserializes the response body from GET/POST /todos endpoints.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TodoResponse {
    private Integer id;
    private Integer userId;
    private String title;
    private Boolean completed;
}
