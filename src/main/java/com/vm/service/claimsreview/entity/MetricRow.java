package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "metric_row")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricRow {

    @Id
    private Long id;

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
}