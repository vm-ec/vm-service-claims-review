package com.vm.service.claimsreview.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class MetaDTO {

    private List<KpiDTO> kpis;
    private Instant timestamp;
}
