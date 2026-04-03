package com.vm.service.claimsreview.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service for managing in-memory metrics cache.
 * Follows Single Responsibility Principle - only handles caching.
 */
@Slf4j
@Service
public class MetricsCacheService {

    private final AtomicReference<Map<String, Object>> cache = new AtomicReference<>();

    /**
     * Store metrics in cache
     * @param metrics Metrics to cache
     */
    public void store(Map<String, Object> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            log.warn("Attempted to cache null or empty metrics");
            return;
        }
        cache.set(metrics);
        log.debug("Cached metrics with {} keys", metrics.size());
    }

    /**
     * Retrieve cached metrics
     * @return Optional containing cached metrics, or empty if not available
     */
    public Optional<Map<String, Object>> retrieve() {
        return Optional.ofNullable(cache.get());
    }

    /**
     * Clear the cache
     */
    public void clear() {
        cache.set(null);
        log.info("Cache cleared");
    }

    /**
     * Check if cache has data
     * @return true if cache contains data
     */
    public boolean hasData() {
        return cache.get() != null;
    }
}
