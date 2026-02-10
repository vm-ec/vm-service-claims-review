package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "metrics_response")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
