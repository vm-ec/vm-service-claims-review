package com.vm.service.claimsreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricSampleDto {
    private String metricName;
    private String metricType;
    private String documentation;
    private Map<String, String> labels;
    private Double value;
    private Long timestamp;
}
