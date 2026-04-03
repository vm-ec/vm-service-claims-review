package com.vm.service.claimsreview.controllers;

import com.vm.service.claimsreview.response.ServiceResponse;
import com.vm.service.claimsreview.service.DashboardMetricsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@Slf4j
@RestController
@CrossOrigin
@AllArgsConstructor
public class DashboardMetricsController {

    private final DashboardMetricsService service;

    @GetMapping("/internal/metrics")
    public ResponseEntity<Map<String, Object>> fetchMetrics() {
        ServiceResponse<Map<String, Object>> response = service.getCurrentMetrics();
        return ResponseEntity.status(response.getHttpStatus()).body(response.getData());
    }

    @PostMapping("/internal/metrics")
    public ResponseEntity<Void> receiveMetrics(@RequestBody Map<String, Object> payload) {
        ServiceResponse<Void> response = service.processIncomingMetrics(payload);
        return ResponseEntity.status(response.getHttpStatus()).build();
    }
}
