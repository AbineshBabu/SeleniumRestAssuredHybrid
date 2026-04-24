package com.framework.dto.response.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AlbumResponse DTO
 * Deserializes the response body from GET/POST /albums endpoints.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumResponse {
    private Integer id;
    private Integer userId;
    private String title;
}
