# Complete Flow: Kafka → Timestream → Dashboard

## Current Architecture

```
Kafka Consumer (External) 
    ↓ POST JSON
DashboardMetricsController (/internal/metrics)
    ↓
    ├─→ Store in Memory (AtomicReference) → Dashboard GET request
    └─→ Store in Timestream (if enabled)
```

## Data Flow

1. **Kafka Consumer** sends JSON (like your `output` file) to `POST http://localhost:8082/internal/metrics`
2. **DashboardMetricsController** receives the payload:
   - Stores in memory for real-time access
   - Calls `MetricsStorageService.storeMetrics()`
3. **TimestreamStorageStrategy** extracts and stores:
   - KPIs (total_requests, avg_latency, error_rate, total_tokens)
   - Model distribution (gemini %, openai %)
   - Latency feed average
4. **Dashboard** fetches from `GET http://localhost:8082/internal/metrics`
   - Returns latest metrics from memory
   - Historical data available via `/api/metrics/historical?days=7`

## Setup Instructions

### 1. Enable Timestream Storage

Edit `application.properties`:
```properties
metrics.storage.type=timestream
aws.region=us-east-1
timestream.database=ai_metrics_db
timestream.table=metrics_table
```

### 2. Create Timestream Resources

```bash
# Create database
aws timestream-write create-database --database-name ai_metrics_db

# Create table
aws timestream-write create-table \
  --database-name ai_metrics_db \
  --table-name metrics_table \
  --retention-properties MemoryStoreRetentionPeriodInHours=24,MagneticStoreRetentionPeriodInDays=90
```

### 3. Configure AWS Credentials

```bash
aws configure
# Enter your AWS Access Key ID
# Enter your AWS Secret Access Key
# Enter region: us-east-1
```

### 4. Start Application

```bash
mvn spring-boot:run
```

## What Gets Stored in Timestream

From your JSON output, these metrics are stored:

### KPIs (4 records per POST)
- `total_requests` = 3
- `avg_latency` = 1590
- `error_rate` = 33.0
- `total_tokens` = 512

### Model Distribution (2 records per POST)
- `model_gemini` = 67
- `model_openai` = 33

### Latency Feed (1 record per POST)
- `latency_feed_avg` = 1717.67 (average of [2499.0, 1206.0, 1448.0])

**Total: 7 records stored per Kafka message**

## API Endpoints

### Receive Metrics from Kafka
```bash
POST http://localhost:8082/internal/metrics
Content-Type: application/json

{
  "meta": {"kpis": [...]},
  "modelDistribution": [...],
  "metricTables": [...],
  "latencyFeed": [...]
}
```

### Get Current Metrics (Dashboard)
```bash
GET http://localhost:8082/internal/metrics
```

### Get Historical Metrics
```bash
# Last 7 days
GET http://localhost:8082/api/metrics/historical?days=7

# Last 30 days
GET http://localhost:8082/api/metrics/historical?days=30
```

### Check Storage Status
```bash
GET http://localhost:8082/api/metrics/storage/status

Response:
{
  "enabled": true,
  "type": "TIMESTREAM"
}
```

## Testing Without Timestream

If you don't want to use Timestream yet:

1. Keep `metrics.storage.type` commented out in `application.properties`
2. Application will work normally
3. Metrics stored only in memory (lost on restart)
4. No AWS credentials needed

## Kafka Consumer Configuration

Your Kafka consumer should POST to:
```
URL: http://localhost:8082/internal/metrics
Method: POST
Content-Type: application/json
Body: <your metrics JSON>
```

## Logs to Monitor

```
INFO  - Received metrics payload from Kafka consumer
INFO  - Stored 7 records to Timestream
DEBUG - Metrics processed successfully
INFO  - Fetching current metrics for dashboard
```

## Query Timestream Data

```sql
-- Get all metrics from last hour
SELECT * FROM "ai_metrics_db"."metrics_table" 
WHERE time > ago(1h) 
ORDER BY time DESC;

-- Get average latency over time
SELECT measure_name, AVG(CAST(measure_value AS DOUBLE)) as avg_value
FROM "ai_metrics_db"."metrics_table"
WHERE measure_name = 'avg_latency'
AND time > ago(24h)
GROUP BY measure_name;

-- Get model distribution trends
SELECT measure_name, measure_value, time
FROM "ai_metrics_db"."metrics_table"
WHERE measure_name LIKE 'model_%'
AND time > ago(7d)
ORDER BY time DESC;
```

## Cost Estimation

### Timestream Pricing (us-east-1)
- **Writes**: $0.50 per million writes
  - 7 records per Kafka message
  - If Kafka sends every 10 seconds: ~60,480 records/day
  - Cost: ~$0.03/day or $0.90/month

- **Storage**: 
  - Memory (24h): $0.036 per GB-hour
  - Magnetic (90d): $0.03 per GB-month
  - Estimated: <$1/month for typical metrics

**Total: ~$2-3/month**

## Troubleshooting

### No data in Timestream
- Check AWS credentials: `aws sts get-caller-identity`
- Verify database/table exist: `aws timestream-write describe-table --database-name ai_metrics_db --table-name metrics_table`
- Check logs for errors

### Dashboard shows "No metrics available"
- Ensure Kafka consumer is POSTing to `/internal/metrics`
- Check application logs for "Received metrics payload"
- Test manually: `curl -X POST http://localhost:8082/internal/metrics -H "Content-Type: application/json" -d @src/main/resources/static/output`

### Storage not enabled
- Verify `metrics.storage.type=timestream` is uncommented
- Restart application
- Check `/api/metrics/storage/status`
