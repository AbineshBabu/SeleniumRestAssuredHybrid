package com.framework.dto.response.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommentResponse DTO
 * Deserializes the response body from GET/POST/PATCH /comments endpoints.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {
    private Integer id;
    private Integer postId;
    private String name;
    private String email;
    private String body;
}
