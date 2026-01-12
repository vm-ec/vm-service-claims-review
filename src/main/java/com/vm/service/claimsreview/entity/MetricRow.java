package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "metric_row")
public class MetricRow {

    @Id
    private Long id; // Provided by response

    private String name;
    private String status;
    private String tech;
    private String model;
    private String latency;
    private String requests;
    private Double drift;
    private String cost;

    @ElementCollection
    @CollectionTable(
            name = "metric_row_logs",
            joinColumns = @JoinColumn(name = "row_id")
    )
    @Column(name = "log_entry")
    private List<String> logs;

    @ManyToOne
    @JoinColumn(name = "metrics_response_id")
    private MetricsResponse metricsResponse;

    /* ===== Getters & Setters ===== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTech() {
        return tech;
    }

    public void setTech(String tech) {
        this.tech = tech;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLatency() {
        return latency;
    }

    public void setLatency(String latency) {
        this.latency = latency;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }

    public Double getDrift() {
        return drift;
    }

    public void setDrift(Double drift) {
        this.drift = drift;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    public MetricsResponse getMetricsResponse() {
        return metricsResponse;
    }

    public void setMetricsResponse(MetricsResponse metricsResponse) {
        this.metricsResponse = metricsResponse;
    }
}