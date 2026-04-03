package com.vm.service.claimsreview.service;

import com.vm.service.claimsreview.storage.MetricsStorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service for managing metrics storage operations.
 * Orchestrates storage strategy selection and delegates to appropriate implementation.
 * Follows Single Responsibility Principle - only manages storage orchestration.
 * Follows Dependency Inversion Principle - depends on MetricsStorageStrategy abstraction.
 */
@Slf4j
@Service
public class MetricsStorageService {

    private final Optional<MetricsStorageStrategy> storageStrategy;

    /**
     * Constructor with automatic strategy selection
     * @param strategies List of available storage strategies (injected by Spring)
     */
    public MetricsStorageService(@Autowired(required = false) List<MetricsStorageStrategy> strategies) {
        this.storageStrategy = strategies != null && !strategies.isEmpty() 
            ? Optional.of(strategies.get(0)) 
            : Optional.empty();
        
        storageStrategy.ifPresentOrElse(
            strategy -> log.info("Initialized with storage type: {}", strategy.getStorageType()),
            () -> log.warn("No storage strategy configured - metrics will not be persisted")
        );
    }

    /**
     * Store metrics using configured strategy
     * @param metrics Metrics to store
     */
    public void storeMetrics(Map<String, Object> metrics) {
        storageStrategy.ifPresentOrElse(
            strategy -> {
                try {
                    strategy.store(metrics);
                } catch (Exception e) {
                    log.error("Storage operation failed", e);
                    throw e;
                }
            },
            () -> log.debug("Storage disabled - metrics not persisted")
        );
    }

    /**
     * Retrieve historical metrics
     * @param days Number of days to retrieve
     * @return List of historical metrics, or empty list if storage disabled
     */
    public List<Map<String, Object>> getHistoricalMetrics(int days) {
        return storageStrategy
            .map(strategy -> {
                try {
                    return strategy.retrieve(days);
                } catch (Exception e) {
                    log.error("Retrieval operation failed", e);
                    throw e;
                }
            })
            .orElseGet(() -> {
                log.debug("Storage disabled - returning empty list");
                return List.of();
            });
    }

    /**
     * Check if storage is enabled
     * @return true if storage strategy is configured
     */
    public boolean isStorageEnabled() {
        return storageStrategy.isPresent();
    }

    /**
     * Get active storage type
     * @return Storage type identifier, or "NONE" if disabled
     */
    public String getActiveStorageType() {
        return storageStrategy
            .map(MetricsStorageStrategy::getStorageType)
            .orElse("NONE");
    }
}
