package com.vm.service.claimsreview.controllers;

import com.vm.service.claimsreview.dto.MetricsResponseDTO;
import com.vm.service.claimsreview.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observe")
@RequiredArgsConstructor
public class ObservabilityController {
    private final MetricsService metricsService;

    @GetMapping("/metrics")
    public ResponseEntity<MetricsResponseDTO> getMetrics() {
        return ResponseEntity.ok(metricsService.getSampleData());
    }
}