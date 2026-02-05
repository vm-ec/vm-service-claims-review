package com.vm.service.claimsreview.dto;

import java.time.Instant;
import java.util.List;

public class MetaDTO {

    private List<KpiDTO> kpis;

    public List<KpiDTO> getKpis() {
        return kpis;
    }

    public void setKpis(List<KpiDTO> kpis) {
        this.kpis = kpis;
    }

    public void setTimestamp(Instant timestamp) {
    }
}
