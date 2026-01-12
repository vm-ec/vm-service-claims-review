package com.vm.service.claimsreview.repository;


import com.vm.service.claimsreview.entity.MetricsResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetricsResponseRepository
        extends JpaRepository<MetricsResponse, Long> {

    Optional<MetricsResponse> findTopByOrderByIdDesc();
}
