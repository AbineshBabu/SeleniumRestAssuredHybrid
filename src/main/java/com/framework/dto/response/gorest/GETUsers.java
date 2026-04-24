package com.framework.dto.response.gorest;

import io.restassured.common.mapper.TypeRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GETUsers {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;
}
