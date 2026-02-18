package com.vm.service.claimsreview.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RestController
@CrossOrigin
public class DashboardMetricsController {

    private final WebClient webClient;
    private final AtomicReference<Map<String, Object>> storedMetrics = new AtomicReference<>();

    @Value("${wrapper.service.url}")
    private String wrapperServiceUrl;

    public DashboardMetricsController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/internal/metrics")
    public String fetchMetrics() {
        try {
            log.info("Fetching metrics from wrapper service");
            
            String metrics = webClient.get()
                    .uri(wrapperServiceUrl)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            log.info("Metrics fetched successfully");
            return metrics;
            
        } catch (Exception e) {
            log.error("Failed to fetch metrics", e);
            throw new RuntimeException("Failed to fetch metrics", e);
        }
    }

    @PostMapping("/internal/metrics")
    public void receiveMetrics(@RequestBody Map<String, Object> payload) {
        log.info("Received metrics payload");
        storedMetrics.set(payload);
        log.debug("Metrics stored successfully");
    }
}
