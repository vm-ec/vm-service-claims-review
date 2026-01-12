package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "metrics_response")
public class MetricsResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "meta_id")
    private MetricsMeta meta;

    @OneToMany(
            mappedBy = "metricsResponse",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MetricColumn> columns;

    @OneToMany(
            mappedBy = "metricsResponse",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MetricRow> rows;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public MetricsMeta getMeta() {
        return meta;
    }

    public void setMeta(MetricsMeta meta) {
        this.meta = meta;
    }

    public List<MetricColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<MetricColumn> columns) {
        this.columns = columns;
    }

    public List<MetricRow> getRows() {
        return rows;
    }

    public void setRows(List<MetricRow> rows) {
        this.rows = rows;
    }
}
