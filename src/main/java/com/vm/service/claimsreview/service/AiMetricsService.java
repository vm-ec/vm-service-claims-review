package com.vm.service.claimsreview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiMetricsService {

    private static final String METRICS_URL = "https://enzymolytic-cristiano-wartier.ngrok-free.dev/json_metrics";

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AiMetricsService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public JsonNode fetchRawMetrics() {

        String response = webClient.get()
                .uri(METRICS_URL)
                .retrieve()
                .bodyToMono(String.class)
                .block();   // OK for dashboard use

        try {
            return objectMapper.readTree(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse metrics JSON", e);
        }
    }
}
