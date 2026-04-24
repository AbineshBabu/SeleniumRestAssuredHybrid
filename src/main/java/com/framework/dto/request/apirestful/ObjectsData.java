package com.framework.dto.request.apirestful;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectsData {

    private int year;
    private int price;

    @JsonProperty("CPU model")
    private String cpu_model;

    @JsonProperty("Hard disk size")
    private String hard_disk_size;
}
