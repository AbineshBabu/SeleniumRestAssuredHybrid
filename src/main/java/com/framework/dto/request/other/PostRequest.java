package com.framework.dto.request.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PostRequest DTO
 * Represents the request body for POST /posts and PATCH /posts/{id}.
 *
 * Lombok annotations:
 *   @Data           → generates getters, setters, equals, hashCode, toString
 *   @Builder        → enables PostRequest.builder().title("...").build()
 *   @NoArgsConstructor / @AllArgsConstructor → needed for Jackson + Builder together
 *
 * @JsonInclude(NON_NULL) ensures PATCH bodies omit null fields automatically —
 * so a patch-only DTO can set just "title" and leave "body"/"userId" null.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostRequest {
    private String title;
    private String body;
    private Integer userId;
}
