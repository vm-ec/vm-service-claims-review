package com.vm.service.claimsreview.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "metric_column")
public class MetricColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "column_key")
    private String columnKey;

    private String label;
    private String type;

    @ManyToOne
    @JoinColumn(name = "metrics_response_id")
    private MetricsResponse metricsResponse;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MetricsResponse getMetricsResponse() {
        return metricsResponse;
    }

    public void setMetricsResponse(MetricsResponse metricsResponse) {
        this.metricsResponse = metricsResponse;
    }
}