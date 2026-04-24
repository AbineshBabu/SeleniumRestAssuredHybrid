package com.framework.dto.response.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PostResponse DTO
 * Deserializes the response body from GET/POST/PATCH /posts endpoints.
 *
 * @JsonIgnoreProperties(ignoreUnknown = true) — safe guard: if the API adds
 * new fields in future, Jackson won't throw; the DTO just ignores them.
 * This is the standard pattern for response DTOs in test frameworks.
 *
 * Usage in steps:
 *   PostResponse post = response.as(PostResponse.class);
 *   assertThat(post.getId()).isNotNull();
 *   assertThat(post.getTitle()).isEqualTo(requestDto.getTitle());
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {
    private Integer id;
    private Integer userId;
    private String title;
    private String body;
}
