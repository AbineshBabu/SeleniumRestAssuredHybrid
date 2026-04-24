package com.framework.dto.request.other;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CommentRequest DTO
 * Represents the request body for POST /comments and PATCH /comments/{id}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequest {
    private Integer postId;
    private String name;
    private String email;
    private String body;
}
