package com.vm.service.claimsreview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MetricRowDTO {
    private String category;
    private String name;
    private String labels;
    private String type;
    private String value;

    public MetricRowDTO(String category, String name, String model, String operation, String value, String unit) {
        this.category = category;
        this.name = name;
        this.labels = model;
        this.type = operation;
        this.value = value + " " + unit;
    }
}
