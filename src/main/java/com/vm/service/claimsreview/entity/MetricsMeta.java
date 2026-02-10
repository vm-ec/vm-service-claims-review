package com.vm.service.claimsreview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "metrics_meta")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
