package com.vm.service.claimsreview.dto;

import java.time.Instant;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricSampleDto {

    private String metricName;
    private String metricType;
    private String documentation;
    private Map<String, String> labels;
    private Double value;
    private Instant timestamp;

}
