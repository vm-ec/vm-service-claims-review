package com.vm.service.claimsreview.service;

import com.vm.service.claimsreview.cache.MetricsCacheService;
import com.vm.service.claimsreview.constants.MetricsConstants;
import com.vm.service.claimsreview.response.ServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for dashboard metrics business logic.
 * Orchestrates caching, validation, and storage operations.
 * Handles all business logic and error handling.
 * Returns ServiceResponse to communicate results without HTTP dependencies.
 */
@Slf4j
@Service
@AllArgsConstructor
public class DashboardMetricsService {

    private final MetricsCacheService cacheService;
    private final MetricsStorageService storageService;
    private final MetricsValidationService validationService;

    /**
     * Get current metrics from cache
     * @return ServiceResponse with metrics or no-data message
     */
    public ServiceResponse<Map<String, Object>> getCurrentMetrics() {
        log.info("Retrieving current metrics from cache");
        
        return cacheService.retrieve()
                .map(metrics -> {
                    log.debug("Returning cached metrics");
                    return ServiceResponse.success(metrics);
                })
                .orElseGet(() -> {
                    log.warn("No metrics available");
                    Map<String, Object> noDataMessage = Map.of(
                        MetricsConstants.KEY_MESSAGE, 
                        MetricsConstants.MESSAGE_NO_METRICS
                    );
                    return ServiceResponse.success(noDataMessage);
                });
    }

    /**
     * Process incoming metrics from Kafka
     * Validates, caches, and stores metrics
     * Handles all exceptions and returns appropriate response
     * @param metrics Metrics payload to process
     * @return ServiceResponse indicating success or failure
     */
    public ServiceResponse<Void> processIncomingMetrics(Map<String, Object> metrics) {
        log.info("Processing incoming metrics payload");

        try {
            // Validate input
            validationService.validate(metrics);
            log.debug("Metrics validation passed");

            // Store in cache for real-time access
            cacheService.store(metrics);
            log.debug("Metrics cached successfully");

            // Persist to configured storage (Timestream/S3)
            storageService.storeMetrics(metrics);
            log.debug("Metrics persisted to storage");

            log.info("Metrics processing completed successfully");
            return ServiceResponse.success(null, HttpStatus.ACCEPTED);
            
        } catch (IllegalArgumentException e) {
            log.error("Validation failed: {}", e.getMessage());
            return ServiceResponse.validationError(e.getMessage());
            
        } catch (Exception e) {
            log.error("Processing failed", e);
            return ServiceResponse.internalError("Failed to process metrics: " + e.getMessage());
        }
    }
}
