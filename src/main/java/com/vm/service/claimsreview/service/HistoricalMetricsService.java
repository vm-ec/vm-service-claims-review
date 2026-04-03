package com.vm.service.claimsreview.service;

import com.vm.service.claimsreview.response.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service for historical metrics business logic.
 * Handles validation, retrieval, and all error handling.
 * Returns ServiceResponse to communicate results without HTTP dependencies.
 */
@Slf4j
@Service
@AllArgsConstructor
public class HistoricalMetricsService {

    private final MetricsStorageService storageService;
    private final MetricsValidationService validationService;

    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 365;

    /**
     * Get historical metrics for specified number of days
     * Handles validation and all exceptions
     * @param days Number of days to retrieve
     * @return ServiceResponse with metrics or error
     */
    public ServiceResponse<List<Map<String, Object>>> getHistoricalMetrics(int days) {
        log.info("Retrieving historical metrics for last {} days", days);
        
        try {
            validateDaysParameter(days);
            List<Map<String, Object>> metrics = storageService.getHistoricalMetrics(days);
            return ServiceResponse.success(metrics);
            
        } catch (IllegalArgumentException e) {
            log.error("Invalid days parameter: {}", e.getMessage());
            return ServiceResponse.validationError(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to retrieve historical metrics", e);
            return ServiceResponse.internalError("Failed to retrieve metrics: " + e.getMessage());
        }
    }

    /**
     * Get storage status information
     * Handles all exceptions
     * @return ServiceResponse with storage status or error
     */
    public ServiceResponse<Map<String, Object>> getStorageStatus() {
        log.debug("Retrieving storage status");
        
        try {
            Map<String, Object> status = Map.of(
                    "enabled", storageService.isStorageEnabled(),
                    "type", storageService.getActiveStorageType()
            );
            return ServiceResponse.success(status);
            
        } catch (Exception e) {
            log.error("Failed to get storage status", e);
            return ServiceResponse.internalError("Failed to get storage status: " + e.getMessage());
        }
    }

    /**
     * Store metrics manually (for testing/admin purposes)
     * Handles validation and all exceptions
     * @param metrics Metrics to store
     * @return ServiceResponse indicating success or failure
     */
    public ServiceResponse<Void> storeMetrics(Map<String, Object> metrics) {
        log.info("Manual storage request");
        
        try {
            validationService.validate(metrics);
            storageService.storeMetrics(metrics);
            log.info("Manual storage completed");
            return ServiceResponse.success(null, HttpStatus.ACCEPTED);
            
        } catch (IllegalArgumentException e) {
            log.error("Validation failed: {}", e.getMessage());
            return ServiceResponse.validationError(e.getMessage());
            
        } catch (Exception e) {
            log.error("Failed to store metrics", e);
            return ServiceResponse.internalError("Failed to store metrics: " + e.getMessage());
        }
    }

    /**
     * Validate days parameter is within acceptable range
     * @param days Number of days
     * @throws IllegalArgumentException if out of range
     */
    private void validateDaysParameter(int days) {
        if (days < MIN_DAYS || days > MAX_DAYS) {
            throw new IllegalArgumentException(
                String.format("Days parameter must be between %d and %d, got: %d", MIN_DAYS, MAX_DAYS, days)
            );
        }
    }
}
