package com.vm.service.claimsreview.entity;

import com.vm.service.claimsreview.entity.KpiMetric;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "metrics_meta")
public class MetricsMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant timestamp;

    @OneToMany(
            mappedBy = "meta",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<KpiMetric> kpis;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public List<KpiMetric> getKpis() {
        return kpis;
    }

    public void setKpis(List<KpiMetric> kpis) {
        this.kpis = kpis;
    }
}
