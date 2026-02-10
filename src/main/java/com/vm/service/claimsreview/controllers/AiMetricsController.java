package com.vm.service.claimsreview.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.vm.service.claimsreview.service.AiMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class AiMetricsController {

    private final AiMetricsService service;

    @GetMapping("/dashboard/original")
    public ResponseEntity<JsonNode> getMetrics() {
        return ResponseEntity.ok(service.fetchRawMetrics());
    }
}

