package com.framework.dto.request.gorest;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class POSTUsersReq {

    private String name;
    private String email;
    private String gender;
    private String status;
}
