package com.vm.service.claimsreview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.service.claimsreview.connector.AiMetricsConnector;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AiMetricsService {

    private final AiMetricsConnector connector;
    private final ObjectMapper objectMapper;

    public JsonNode fetchRawMetrics() {
        try {
            String response = connector.fetchMetrics();
            return objectMapper.readTree(response);
        } catch (Exception e) {
            log.error("Failed to parse metrics JSON", e);
            throw new RuntimeException("Failed to parse metrics JSON", e);
        }
    }
}
