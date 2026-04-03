package com.vm.service.claimsreview.constants;

/**
 * Constants for metrics processing.
 * Centralizes all magic strings and numbers to improve maintainability.
 */
public final class MetricsConstants {

    private MetricsConstants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }

    // JSON Keys - Top Level
    public static final String KEY_META = "meta";
    public static final String KEY_KPIS = "kpis";
    public static final String KEY_MODEL_DISTRIBUTION = "modelDistribution";
    public static final String KEY_LATENCY_FEED = "latencyFeed";
    public static final String KEY_MESSAGE = "message";

    // JSON Keys - KPI Fields
    public static final String KEY_KPI_ID = "id";
    public static final String KEY_KPI_VALUE = "value";
    public static final String KEY_KPI_TITLE = "title";

    // JSON Keys - Model Distribution
    public static final String KEY_MODEL = "model";
    public static final String KEY_PERCENTAGE = "percentage";

    // Timestream Measure Names
    public static final String MEASURE_PREFIX_MODEL = "model_";
    public static final String MEASURE_LATENCY_FEED_AVG = "latency_feed_avg";

    // S3 Path Components
    public static final String S3_PREFIX_METRICS = "metrics/";
    public static final String S3_FILE_EXTENSION = ".json";
    public static final String S3_PATH_SEPARATOR = "/";

    // Messages
    public static final String MESSAGE_NO_METRICS = "No metrics available";

    // Storage Types
    public static final String STORAGE_TYPE_TIMESTREAM = "TIMESTREAM";
    public static final String STORAGE_TYPE_S3 = "S3";
    public static final String STORAGE_TYPE_NONE = "NONE";

    // Timestream Query
    public static final String TIMESTREAM_QUERY_TEMPLATE = 
        "SELECT measure_name, measure_value::varchar, time " +
        "FROM \"%s\".\"%s\" " +
        "WHERE time > ago(%dd) " +
        "ORDER BY time DESC";

    // Result Keys
    public static final String RESULT_KEY_METRIC = "metric";
    public static final String RESULT_KEY_VALUE = "value";
    public static final String RESULT_KEY_TIMESTAMP = "timestamp";

    // Special Characters
    public static final String PERCENT_SIGN = "%";
    public static final String EMPTY_STRING = "";

    // Content Types
    public static final String CONTENT_TYPE_JSON = "application/json";
}
