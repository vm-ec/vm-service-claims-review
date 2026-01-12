package com.vm.service.claimsreview.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.vm.service.claimsreview.service.AiMetricsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/metrics")
public class AiMetricsController {

    private final AiMetricsService service;

    public AiMetricsController(AiMetricsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<JsonNode> getMetrics() {
        return ResponseEntity.ok(service.fetchRawMetrics());
    }
}

