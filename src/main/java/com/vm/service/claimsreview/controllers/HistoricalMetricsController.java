package com.vm.service.claimsreview.controllers;

import com.vm.service.claimsreview.response.ServiceResponse;
import com.vm.service.claimsreview.service.HistoricalMetricsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/metrics")
@AllArgsConstructor
public class HistoricalMetricsController {

    private final HistoricalMetricsService service;

    @GetMapping("/historical")
    public ResponseEntity<List<Map<String, Object>>> getHistoricalMetrics(
            @RequestParam(defaultValue = "7") int days) {
        ServiceResponse<List<Map<String, Object>>> response = service.getHistoricalMetrics(days);
        return ResponseEntity.status(response.getHttpStatus()).body(response.getData());
    }

    @GetMapping("/storage/status")
    public ResponseEntity<Map<String, Object>> getStorageStatus() {
        ServiceResponse<Map<String, Object>> response = service.getStorageStatus();
        return ResponseEntity.status(response.getHttpStatus()).body(response.getData());
    }

    @PostMapping("/store")
    public ResponseEntity<Void> storeMetrics(@RequestBody Map<String, Object> metrics) {
        ServiceResponse<Void> response = service.storeMetrics(metrics);
        return ResponseEntity.status(response.getHttpStatus()).build();
    }
}
