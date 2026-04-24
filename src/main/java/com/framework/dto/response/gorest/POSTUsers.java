package com.framework.dto.response.gorest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class POSTUsers {

    private int id;
    private String name;
    private String email;
    private String gender;
    private String status;
}
