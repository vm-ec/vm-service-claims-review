package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kpi_metric")
public class KpiMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kpi_id")
    private String kpiId;

    private String title;
    
    @Column(name = "kpi_value")
    private String value;
    
    private String trend;
    private String trendType;
    private Boolean isFinancial;

    @ManyToOne
    @JoinColumn(name = "meta_id")
    private MetricsMeta meta;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public String getKpiId() {
        return kpiId;
    }

    public void setKpiId(String kpiId) {
        this.kpiId = kpiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public String getTrendType() {
        return trendType;
    }

    public void setTrendType(String trendType) {
        this.trendType = trendType;
    }

    public Boolean getIsFinancial() {
        return isFinancial;
    }

    public void setIsFinancial(Boolean isFinancial) {
        this.isFinancial = isFinancial;
    }

    public MetricsMeta getMeta() {
        return meta;
    }

    public void setMeta(MetricsMeta meta) {
        this.meta = meta;
    }
}
