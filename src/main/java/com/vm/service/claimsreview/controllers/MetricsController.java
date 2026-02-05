package com.vm.service.claimsreview.controllers;

import com.vm.service.claimsreview.dto.MetricsResponseDTO;
import com.vm.service.claimsreview.service.MetricsParsingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final MetricsParsingService parsingService;

    public MetricsController(MetricsParsingService parsingService) {
        this.parsingService = parsingService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<MetricsResponseDTO> getDashboardMetrics() {
        MetricsResponseDTO response = parsingService.fetchAndParseDashboard();
        return ResponseEntity.ok(response);
    }
}
