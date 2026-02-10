package com.vm.service.claimsreview.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metric_column")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}