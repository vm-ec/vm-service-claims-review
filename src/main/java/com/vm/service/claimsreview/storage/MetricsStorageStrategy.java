package com.vm.service.claimsreview.storage;

import java.util.List;
import java.util.Map;

/**
 * Strategy interface for metrics storage implementations.
 * Follows Strategy Pattern - allows different storage backends to be used interchangeably.
 * Follows Open/Closed Principle - new storage types can be added without modifying existing code.
 */
public interface MetricsStorageStrategy {
    
    /**
     * Store metrics to the storage backend
     * @param metrics Metrics data to store (JSON structure as Map)
     * @throws RuntimeException if storage operation fails
     */
    void store(Map<String, Object> metrics);
    
    /**
     * Retrieve historical metrics from storage
     * @param days Number of days of historical data to retrieve
     * @return List of metrics records, each as a Map
     * @throws RuntimeException if retrieval operation fails
     */
    List<Map<String, Object>> retrieve(int days);
    
    /**
     * Get the type of storage backend
     * @return Storage type identifier (e.g., "TIMESTREAM", "S3", "NONE")
     */
    String getStorageType();
}
