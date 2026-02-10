package com.vm.service.claimsreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDTO {
    private String id;
    private String title;
    private String value;
    private String trend;
    private String trendType;
    private Boolean isFinancial;
}