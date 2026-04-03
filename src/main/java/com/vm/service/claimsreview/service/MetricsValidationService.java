package com.vm.service.claimsreview.service;

import com.vm.service.claimsreview.constants.MetricsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for validating metrics payloads.
 * Follows Single Responsibility Principle - only validates input.
 */
@Slf4j
@Service
public class MetricsValidationService {

    private static final int MAX_PAYLOAD_SIZE = 1_000_000; // 1MB in characters

    /**
     * Validate metrics payload
     * @param metrics Metrics to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validate(Map<String, Object> metrics) {
        if (metrics == null) {
            throw new IllegalArgumentException("Metrics payload cannot be null");
        }

        if (metrics.isEmpty()) {
            throw new IllegalArgumentException("Metrics payload cannot be empty");
        }

        validateSize(metrics);
        validateStructure(metrics);
    }

    /**
     * Validate payload size to prevent memory issues
     */
    private void validateSize(Map<String, Object> metrics) {
        String jsonString = metrics.toString();
        if (jsonString.length() > MAX_PAYLOAD_SIZE) {
            throw new IllegalArgumentException(
                String.format("Payload size exceeds maximum allowed size of %d characters", MAX_PAYLOAD_SIZE)
            );
        }
    }

    /**
     * Validate basic structure of metrics payload
     */
    private void validateStructure(Map<String, Object> metrics) {
        // Basic structure validation - can be extended
        if (!metrics.containsKey(MetricsConstants.KEY_META) && 
            !metrics.containsKey(MetricsConstants.KEY_MODEL_DISTRIBUTION) && 
            !metrics.containsKey(MetricsConstants.KEY_LATENCY_FEED)) {
            log.warn("Metrics payload missing expected keys, but accepting for flexibility");
        }
    }

    /**
     * Check if metrics payload is valid without throwing exception
     * @param metrics Metrics to check
     * @return true if valid, false otherwise
     */
    public boolean isValid(Map<String, Object> metrics) {
        try {
            validate(metrics);
            return true;
        } catch (IllegalArgumentException e) {
            log.debug("Validation failed: {}", e.getMessage());
            return false;
        }
    }
}
