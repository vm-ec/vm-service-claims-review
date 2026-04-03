package com.vm.service.claimsreview.storage;

import com.vm.service.claimsreview.constants.MetricsConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.timestreamquery.TimestreamQueryClient;
import software.amazon.awssdk.services.timestreamquery.model.QueryRequest;
import software.amazon.awssdk.services.timestreamquery.model.QueryResponse;
import software.amazon.awssdk.services.timestreamquery.model.Row;
import software.amazon.awssdk.services.timestreamwrite.TimestreamWriteClient;
import software.amazon.awssdk.services.timestreamwrite.model.MeasureValueType;
import software.amazon.awssdk.services.timestreamwrite.model.Record;
import software.amazon.awssdk.services.timestreamwrite.model.TimeUnit;
import software.amazon.awssdk.services.timestreamwrite.model.WriteRecordsRequest;

import java.util.*;

/**
 * AWS Timestream storage strategy implementation.
 * Stores metrics in AWS Timestream time-series database.
 * Follows Single Responsibility Principle - only handles Timestream operations.
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "metrics.storage.type", havingValue = "timestream")
public class TimestreamStorageStrategy implements MetricsStorageStrategy {

    private final TimestreamWriteClient writeClient;
    private final TimestreamQueryClient queryClient;

    @Value("${timestream.database}")
    private String database;

    @Value("${timestream.table}")
    private String table;

    public TimestreamStorageStrategy(TimestreamWriteClient writeClient, TimestreamQueryClient queryClient) {
        this.writeClient = writeClient;
        this.queryClient = queryClient;
    }

    /**
     * Store metrics in Timestream
     * @param metrics Metrics to store
     */
    @Override
    public void store(Map<String, Object> metrics) {
        try {
            List<Record> records = new ArrayList<>();
            long timestamp = System.currentTimeMillis();

            records.addAll(extractKpiRecords(metrics, timestamp));
            records.addAll(extractModelDistributionRecords(metrics, timestamp));
            records.addAll(extractLatencyRecords(metrics, timestamp));

            if (!records.isEmpty()) {
                persistRecords(records);
            } else {
                log.debug("No records to store");
            }
        } catch (Exception e) {
            log.error("Failed to store metrics to Timestream", e);
            throw new RuntimeException("Timestream storage failed", e);
        }
    }

    /**
     * Retrieve historical metrics from Timestream
     * @param days Number of days to retrieve
     * @return List of historical metrics
     */
    @Override
    public List<Map<String, Object>> retrieve(int days) {
        try {
            String query = String.format(MetricsConstants.TIMESTREAM_QUERY_TEMPLATE, database, table, days);

            QueryRequest queryRequest = QueryRequest.builder()
                    .queryString(query)
                    .build();

            QueryResponse response = queryClient.query(queryRequest);
            List<Map<String, Object>> results = parseQueryResults(response);

            log.info("Retrieved {} records from Timestream for last {} days", results.size(), days);
            return results;
        } catch (Exception e) {
            log.error("Failed to retrieve metrics from Timestream", e);
            throw new RuntimeException("Timestream retrieval failed", e);
        }
    }

    @Override
    public String getStorageType() {
        return MetricsConstants.STORAGE_TYPE_TIMESTREAM;
    }

    /**
     * Extract KPI records from metrics
     */
    private List<Record> extractKpiRecords(Map<String, Object> metrics, long timestamp) {
        List<Record> records = new ArrayList<>();

        Map<String, Object> meta = getMapValue(metrics, MetricsConstants.KEY_META);
        if (meta == null) {
            return records;
        }

        List<Map<String, Object>> kpis = getListValue(meta, MetricsConstants.KEY_KPIS);
        if (kpis == null) {
            return records;
        }

        for (Map<String, Object> kpi : kpis) {
            String id = getStringValue(kpi, MetricsConstants.KEY_KPI_ID);
            String value = getStringValue(kpi, MetricsConstants.KEY_KPI_VALUE);
            
            if (id != null && value != null) {
                String cleanValue = value.replace(MetricsConstants.PERCENT_SIGN, MetricsConstants.EMPTY_STRING);
                records.add(createRecord(id, cleanValue, timestamp));
            }
        }

        return records;
    }

    /**
     * Extract model distribution records from metrics
     */
    private List<Record> extractModelDistributionRecords(Map<String, Object> metrics, long timestamp) {
        List<Record> records = new ArrayList<>();

        List<Map<String, Object>> modelDist = getListValue(metrics, MetricsConstants.KEY_MODEL_DISTRIBUTION);
        if (modelDist == null) {
            return records;
        }

        for (Map<String, Object> model : modelDist) {
            String modelName = getStringValue(model, MetricsConstants.KEY_MODEL);
            Object percentage = model.get(MetricsConstants.KEY_PERCENTAGE);
            
            if (modelName != null && percentage != null) {
                String measureName = MetricsConstants.MEASURE_PREFIX_MODEL + modelName;
                records.add(createRecord(measureName, String.valueOf(percentage), timestamp));
            }
        }

        return records;
    }

    /**
     * Extract latency records from metrics
     */
    private List<Record> extractLatencyRecords(Map<String, Object> metrics, long timestamp) {
        List<Record> records = new ArrayList<>();

        List<Double> latencyFeed = getListValue(metrics, MetricsConstants.KEY_LATENCY_FEED);
        if (latencyFeed == null || latencyFeed.isEmpty()) {
            return records;
        }

        double avgLatency = latencyFeed.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        records.add(createRecord(
            MetricsConstants.MEASURE_LATENCY_FEED_AVG,
            String.valueOf(avgLatency),
            timestamp
        ));

        return records;
    }

    /**
     * Create a Timestream record
     */
    private Record createRecord(String measureName, String measureValue, long timestamp) {
        return Record.builder()
                .measureName(measureName)
                .measureValue(measureValue)
                .measureValueType(MeasureValueType.VARCHAR)
                .time(String.valueOf(timestamp))
                .timeUnit(TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Persist records to Timestream
     */
    private void persistRecords(List<Record> records) {
        WriteRecordsRequest request = WriteRecordsRequest.builder()
                .databaseName(database)
                .tableName(table)
                .records(records)
                .build();

        writeClient.writeRecords(request);
        log.info("Stored {} records to Timestream", records.size());
    }

    /**
     * Parse query results into list of maps
     */
    private List<Map<String, Object>> parseQueryResults(QueryResponse response) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (Row row : response.rows()) {
            if (row.data().size() >= 3) {
                Map<String, Object> record = new HashMap<>();
                record.put(MetricsConstants.RESULT_KEY_METRIC, row.data().get(0).scalarValue());
                record.put(MetricsConstants.RESULT_KEY_VALUE, row.data().get(1).scalarValue());
                record.put(MetricsConstants.RESULT_KEY_TIMESTAMP, row.data().get(2).scalarValue());
                results.add(record);
            }
        }
        
        return results;
    }

    /**
     * Safely get Map value from parent map
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> getMapValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof Map) ? (Map<String, Object>) value : null;
    }

    /**
     * Safely get List value from map
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getListValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return (value instanceof List) ? (List<T>) value : null;
    }

    /**
     * Safely get String value from map
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? String.valueOf(value) : null;
    }
}
