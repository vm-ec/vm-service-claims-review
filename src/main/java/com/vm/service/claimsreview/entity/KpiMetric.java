package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kpi_metric")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
