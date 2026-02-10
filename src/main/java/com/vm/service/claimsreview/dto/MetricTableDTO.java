package com.vm.service.claimsreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricTableDTO {
    private String title;
    private List<MetricRowDTO> rows;
}
