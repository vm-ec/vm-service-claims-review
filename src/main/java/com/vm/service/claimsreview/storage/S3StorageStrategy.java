package com.vm.service.claimsreview.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vm.service.claimsreview.constants.MetricsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * AWS S3 storage strategy implementation.
 * Stores metrics in AWS S3 for long-term archival.
 * Follows Single Responsibility Principle - only handles S3 operations.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "metrics.storage.type", havingValue = "s3")
public class S3StorageStrategy implements MetricsStorageStrategy {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    @Value("${s3.bucket.name}")
    private String bucketName;

    public S3StorageStrategy(S3Client s3Client, ObjectMapper objectMapper) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
    }

    /**
     * Store metrics in S3
     * @param metrics Metrics to store
     */
    @Override
    public void store(Map<String, Object> metrics) {
        try {
            String key = generateS3Key();
            String json = objectMapper.writeValueAsString(metrics);

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(MetricsConstants.CONTENT_TYPE_JSON)
                    .build();

            s3Client.putObject(request, RequestBody.fromString(json));
            log.info("Stored metrics to S3: {}", key);
        } catch (Exception e) {
            log.error("Failed to store metrics to S3", e);
            throw new RuntimeException("S3 storage failed", e);
        }
    }

    /**
     * Retrieve historical metrics from S3
     * @param days Number of days to retrieve
     * @return List of historical metrics
     */
    @Override
    public List<Map<String, Object>> retrieve(int days) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        try {
            LocalDate startDate = LocalDate.now().minusDays(days);
            
            for (int i = 0; i <= days; i++) {
                LocalDate currentDate = startDate.plusDays(i);
                results.addAll(retrieveMetricsForDate(currentDate));
            }

            log.info("Retrieved {} records from S3 for last {} days", results.size(), days);
        } catch (Exception e) {
            log.error("Failed to retrieve metrics from S3", e);
            throw new RuntimeException("S3 retrieval failed", e);
        }
        
        return results;
    }

    @Override
    public String getStorageType() {
        return MetricsConstants.STORAGE_TYPE_S3;
    }

    /**
     * Generate S3 key for storing metrics
     */
    private String generateS3Key() {
        String date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        long timestamp = System.currentTimeMillis();
        return MetricsConstants.S3_PREFIX_METRICS + 
               date + 
               MetricsConstants.S3_PATH_SEPARATOR + 
               timestamp + 
               MetricsConstants.S3_FILE_EXTENSION;
    }

    /**
     * Retrieve metrics for a specific date
     */
    private List<Map<String, Object>> retrieveMetricsForDate(LocalDate date) {
        List<Map<String, Object>> results = new ArrayList<>();
        String dateStr = date.format(DateTimeFormatter.ISO_DATE);
        String prefix = MetricsConstants.S3_PREFIX_METRICS + dateStr + MetricsConstants.S3_PATH_SEPARATOR;

        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

            for (S3Object s3Object : listResponse.contents()) {
                Map<String, Object> data = retrieveObject(s3Object.key());
                if (data != null) {
                    results.add(data);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to retrieve metrics for date {}: {}", dateStr, e.getMessage());
        }

        return results;
    }

    /**
     * Retrieve and parse a single S3 object
     */
    private Map<String, Object> retrieveObject(String key) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            String json = s3Client.getObjectAsBytes(getRequest).asUtf8String();
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            log.warn("Failed to retrieve object {}: {}", key, e.getMessage());
            return null;
        }
    }
}
