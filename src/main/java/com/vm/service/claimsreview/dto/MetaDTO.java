package com.vm.service.claimsreview.dto;

import java.time.Instant;
import java.util.List;

public class MetaDTO {
    private Instant timestamp;
    private List<KpiDTO> kpis;
    // getters & setters
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public List<KpiDTO> getKpis() {
        return kpis;
    }
    public void setKpis(List<KpiDTO> kpis) {
        this.kpis = kpis;
    }

}
