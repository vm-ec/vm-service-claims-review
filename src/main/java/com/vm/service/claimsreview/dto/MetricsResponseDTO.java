package com.vm.service.claimsreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricsResponseDTO {
    private MetaDTO meta;
    private List<ColumnDTO> columns;
    private List<RowDTO> rows;
    private List<MetricTableDTO> metricTables;
}
