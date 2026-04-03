# Project Summary: AI Metrics Observability Dashboard

## 🔹 1. Project Overview

**Project Type:** Spring Boot 3.5.7 REST API + Real-time Dashboard  
**Java Version:** 17  
**Main Purpose:** AI Apps Observability Dashboard that collects, stores, and visualizes AI model performance metrics in real-time

**What it does:**
- Receives AI metrics from Kafka consumer (via external Kafka service)
- Parses Prometheus-style metrics from AI agent wrapper service
- Stores metrics in AWS Timestream (optional) for historical analysis
- Serves real-time dashboard with KPIs, charts, and performance data
- Tracks AI model usage (Gemini, GPT-4, Claude), token consumption, latency, and costs

**Note:** Despite the repository name "vm-service-claims-review", this is actually an **AI Observability Platform**, not a claims review system.

**Part of Larger Ecosystem:**
This service is part of a 3-service architecture:
1. **vm-agent-wrapper** (Port 8083) - LLM gateway that processes AI requests
2. **kafka-metrics-service** - Kafka bridge that polls wrapper and publishes to Kafka
3. **vm-service-claims-review** (Port 8082) - THIS SERVICE - Dashboard and storage

---

## 🔹 2. Entry Point(s)

### Main Application Class
**File:** `ClaimsreviewApplication.java`
```java
@SpringBootApplication
public class ClaimsreviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClaimsreviewApplication.class, args);
    }
}
```

### Application Startup
1. Spring Boot initializes on **port 8082**
2. Loads configuration from `application.properties`
3. Initializes beans:
   - WebClient for HTTP communication
   - MetricsStorageService (with optional Timestream/S3 strategy)
   - All controllers, services, and repositories
4. Serves static dashboard at `src/main/resources/static/index.html`

---

## 🔹 3. High-Level Architecture

**Architecture Pattern:** Layered Architecture with Strategy Pattern for Storage

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  Controllers: DashboardMetricsController, HistoricalMetrics │
├─────────────────────────────────────────────────────────────┤
│                     SERVICE LAYER                            │
│  MetricsStorageService, MetricsParsingService, AiMetrics    │
├─────────────────────────────────────────────────────────────┤
│                   INTEGRATION LAYER                          │
│  AiMetricsConnector (HTTP), Storage Strategies (AWS)        │
├─────────────────────────────────────────────────────────────┤
│                   PERSISTENCE LAYER                          │
│  H2 Database (JPA), AWS Timestream, AWS S3                  │
└─────────────────────────────────────────────────────────────┘
```

### Major Layers/Modules

1. **Controllers** (`controllers/`)
   - REST API endpoints for metrics ingestion and retrieval

2. **Services** (`service/`)
   - Business logic for parsing, storing, and retrieving metrics

3. **Storage** (`storage/`)
   - Pluggable storage strategies (Timestream, S3)

4. **Connectors** (`connector/`)
   - HTTP client for external service communication

5. **DTOs** (`dto/`)
   - Data Transfer Objects for API communication

6. **Entities** (`entity/`)
   - JPA entities for H2 database persistence

7. **Configuration** (`configrs/`)
   - Spring configuration for CORS, WebClient, AWS clients

---

## 🔹 4. End-to-End Flow (CRITICAL)

### **COMPLETE SYSTEM FLOW (All 3 Services)**

```
👤 User/Agent
    ↓ POST /ai/process {input, model, agentName}
vm-agent-wrapper (Port 8083)
    ↓ Routes to OpenAI/Gemini API
    ↓ Calculates tokens, cost, latency
    ↓ Records metrics in Prometheus format
    ↓ Exposes at GET /ai/metrics
    ↓
kafka-metrics-service
    ↓ MetricsScheduler polls every 10 seconds
    ↓ GET http://localhost:8083/ai/metrics
    ↓ MetricsProducer publishes to Kafka topic "ai-metrics"
    ↓
Kafka Broker (Topic: ai-metrics)
    ↓
kafka-metrics-service
    ↓ MetricsConsumer subscribes to "ai-metrics"
    ↓ POST http://localhost:8082/internal/metrics
    ↓
vm-service-claims-review (Port 8082) - THIS SERVICE
    ↓ DashboardMetricsController.receiveMetrics()
    ↓ Store in AtomicReference (in-memory)
    ↓ MetricsStorageService.storeMetrics()
    ↓ TimestreamStorageStrategy.store() [if enabled]
    ↓
AWS Timestream (7 records per message)
    ↓
📊 Frontend Dashboard (index.html)
    ↓ Polls GET /internal/metrics every 10 seconds
    ↓ Updates charts and KPIs in real-time
```

### Flow 1: Kafka → Dashboard (Real-time Metrics)

```
External Kafka Consumer (Port 8083)
    ↓ POST JSON payload
DashboardMetricsController.receiveMetrics()
    ↓
    ├─→ Store in AtomicReference (in-memory cache)
    └─→ MetricsStorageService.storeMetrics()
            ↓
        TimestreamStorageStrategy.store() [if enabled]
            ↓
        AWS Timestream (7 records per message)

Frontend Dashboard (index.html)
    ↓ GET /internal/metrics (every 10 seconds)
DashboardMetricsController.fetchMetrics()
    ↓
Returns cached metrics from AtomicReference
    ↓
Dashboard updates charts and KPIs
```

**Actual Files Involved:**
1. `DashboardMetricsController.java` - Receives POST, serves GET
2. `MetricsStorageService.java` - Routes to storage strategy
3. `TimestreamStorageStrategy.java` - Writes to AWS Timestream
4. `index.html` - Frontend dashboard with Chart.js

### Flow 2: AI Agent Wrapper → Parsed Metrics

```
AI Agent Wrapper Service (Port 8083)
    ↓ Prometheus metrics
AiMetricsConnector.fetchMetrics()
    ↓ HTTP GET
AiMetricsService.fetchRawMetrics()
    ↓ JSON parsing
MetricsParsingService.parseMetrics()
    ↓ Extract KPIs, counters, histograms
MetricsResponseDTO (structured data)
    ↓
Return to caller
```

**Actual Files Involved:**
1. `AiMetricsConnector.java` - HTTP client
2. `AiMetricsService.java` - Fetches raw metrics
3. `MetricsParsingService.java` - Parses Prometheus format
4. `MetricsResponseDTO.java` - Structured response

### Flow 3: Historical Data Retrieval

```
Frontend Request
    ↓ GET /metrics/historical?days=7
HistoricalMetricsController.getHistoricalMetrics()
    ↓
MetricsStorageService.getHistoricalMetrics()
    ↓
TimestreamStorageStrategy.retrieve()
    ↓ SQL query to Timestream
AWS Timestream Database
    ↓
Return List<Map<String, Object>>
```

**Actual Files Involved:**
1. `HistoricalMetricsController.java` - API endpoint
2. `MetricsStorageService.java` - Service layer
3. `TimestreamStorageStrategy.java` - Query execution

---

## 🔹 5. Key Components & Their Responsibilities

### Controllers

| File | Responsibility |
|------|---------------|
| `DashboardMetricsController.java` | **Main entry point** - Receives metrics from Kafka (POST), serves dashboard (GET) |
| `HistoricalMetricsController.java` | Provides historical metrics API, storage status check |
| `AiMetricsController.java` | Fetches and parses metrics from AI agent wrapper |
| `MetricsController.java` | Legacy metrics endpoints |
| `ObservabilityController.java` | Health check and observability endpoints |
| `HealthController.java` | Service health validation |

### Services

| File | Responsibility |
|------|---------------|
| `MetricsStorageService.java` | **Core storage orchestrator** - Routes metrics to active storage strategy (Timestream/S3/None) |
| `MetricsParsingService.java` | **Prometheus parser** - Parses raw metrics into KPIs, counters, histograms, gauges |
| `AiMetricsService.java` | Fetches raw metrics from AI agent wrapper via HTTP |
| `MetricsService.java` | JPA-based metrics persistence to H2 database |
| `ObservabilityService.java` | Observability and monitoring logic |
| `MetricsParsingServiceHelper.java` | Provides hardcoded fallback data when external service fails |

### Storage Strategies (Strategy Pattern)

| File | Responsibility |
|------|---------------|
| `MetricsStorageStrategy.java` | **Interface** - Contract for all storage implementations |
| `TimestreamStorageStrategy.java` | **AWS Timestream** - Stores KPIs, model distribution, latency feed (7 records/message) |
| `S3StorageStrategy.java` | **AWS S3** - Stores full JSON payloads for archival (future use) |

**Activation:** Controlled by `metrics.storage.type` property (timestream/s3/none)

### Connectors

| File | Responsibility |
|------|---------------|
| `AiMetricsConnector.java` | **HTTP client** - GET/POST/PUT/DELETE methods with dynamic headers and query params |

### DTOs (Data Transfer Objects)

| File | Purpose |
|------|---------|
| `MetricsResponseDTO.java` | Top-level response containing meta, columns, rows, metricTables |
| `MetaDTO.java` | Contains KPIs and timestamp |
| `KpiDTO.java` | Individual KPI (id, title, value, trend, trendType, isFinancial) |
| `MetricTableDTO.java` | Table of metrics grouped by category |
| `MetricRowDTO.java` | Individual metric row |
| `RowDTO.java` | Generic row data |
| `ColumnDTO.java` | Column definition (key, label, type) |

**All DTOs use Lombok:** `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`

### Configuration

| File | Purpose |
|------|---------|
| `AwsStorageConfig.java` | Conditional AWS client beans (Timestream, S3) based on `metrics.storage.type` |
| `WebClientConfig.java` | WebClient bean for HTTP communication |
| `CorsConfig.java` | CORS configuration for frontend access |

---

## 🔹 6. Data Flow

### Incoming Data (from Kafka)

```json
{
  "meta": {
    "kpis": [
      {"id": "total_requests", "title": "Total Requests", "value": "3"},
      {"id": "avg_latency", "title": "Average Latency", "value": "1590"},
      {"id": "error_rate", "title": "Error Rate", "value": "33.0"},
      {"id": "total_tokens", "title": "Total Tokens", "value": "512"}
    ]
  },
  "modelDistribution": [
    {"model": "gemini", "percentage": 67},
    {"model": "openai", "percentage": 33}
  ],
  "latencyFeed": [2499.0, 1206.0, 1448.0],
  "metricTables": [...]
}
```

### Transformation to Timestream

**7 records stored per Kafka message:**
1. `total_requests` = 3
2. `avg_latency` = 1590
3. `error_rate` = 33.0
4. `total_tokens` = 512
5. `model_gemini` = 67
6. `model_openai` = 33
7. `latency_feed_avg` = 1717.67

### Prometheus Metrics Parsing

**Input:** Raw Prometheus metrics from AI agent wrapper
```
ai_requests_total{model="gemini",operation="generate"} 150
ai_request_duration_seconds_sum{model="gemini"} 450.5
ai_request_duration_seconds_count{model="gemini"} 150
ai_tokens_total{model="gemini",type="input"} 12500
```

**Output:** Structured DTOs with calculated KPIs
- Total Requests = sum of all counters
- Average Latency = histogram_sum / histogram_count
- Token Usage = sum of input/output tokens by model

---

## 🔹 7. External Integrations

### 1. Kafka Consumer (External Service)
- **Purpose:** Sends processed metrics to dashboard
- **Endpoint:** `POST http://localhost:8082/internal/metrics`
- **Data:** JSON payload with KPIs, model distribution, latency feed

### 2. AI Agent Wrapper Service
- **Purpose:** Provides raw Prometheus metrics from AI models
- **Endpoint:** `http://localhost:8083/ai/metrics`
- **Protocol:** HTTP GET
- **Data Format:** Prometheus text format

### 3. AWS Timestream (Optional)
- **Purpose:** Time-series database for historical metrics
- **Database:** `ai_metrics_db`
- **Table:** `metrics_table`
- **Retention:** 24h memory store, 90d magnetic store
- **Cost:** ~$2-3/month for typical usage

### 4. AWS S3 (Optional, Future)
- **Purpose:** Long-term archival of full JSON payloads
- **Bucket:** `ai-metrics-bucket`
- **Format:** JSON files organized by date

### 5. H2 Database (In-Memory)
- **Purpose:** JPA entity persistence (legacy)
- **URL:** `jdbc:h2:mem:testdb`
- **Console:** Enabled at `/h2-console`

---

## 🔹 8. Configuration & Setup

### application.properties

```properties
# Server
spring.application.name=claimsreview
server.port=8082

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# External Services
aimetrics.url=https://nzymolytic-cristiano-wartier.ngrok-free.dev/json_metrics
wrapper.service.url=http://localhost:8083/ai/metrics

# AWS Timestream Storage (Optional - Commented by default)
#metrics.storage.type=timestream
#aws.region=us-east-1
#timestream.database=ai_metrics_db
#timestream.table=metrics_table
```

### Key Configuration Points

| Property | Default | Purpose |
|----------|---------|---------|
| `server.port` | 8082 | Application port |
| `wrapper.service.url` | localhost:8083 | AI agent wrapper endpoint |
| `metrics.storage.type` | none | Storage backend (timestream/s3/none) |
| `aws.region` | us-east-1 | AWS region for Timestream/S3 |

---

## 🔹 9. Build & Run Instructions

### Prerequisites
- Java 17
- Maven 3.6+
- (Optional) AWS CLI configured with credentials

### Build
```bash
mvn clean install
```

### Run (Without Storage)
```bash
mvn spring-boot:run
```

Application starts on `http://localhost:8082`

### Run (With Timestream Storage)

1. **Uncomment in application.properties:**
```properties
metrics.storage.type=timestream
aws.region=us-east-1
timestream.database=ai_metrics_db
timestream.table=metrics_table
```

2. **Create AWS Resources:**
```bash
# Create database
aws timestream-write create-database --database-name ai_metrics_db

# Create table
aws timestream-write create-table \
  --database-name ai_metrics_db \
  --table-name metrics_table \
  --retention-properties MemoryStoreRetentionPeriodInHours=24,MagneticStoreRetentionPeriodInDays=90
```

3. **Configure AWS Credentials:**
```bash
aws configure
```

4. **Start Application:**
```bash
mvn spring-boot:run
```

### Test Endpoints

```bash
# Check storage status
curl http://localhost:8082/metrics/storage/status

# Send test metrics (simulate Kafka)
curl -X POST http://localhost:8082/internal/metrics \
  -H "Content-Type: application/json" \
  -d @src/main/resources/static/output

# Get current metrics
curl http://localhost:8082/internal/metrics

# Get historical metrics (last 7 days)
curl http://localhost:8082/metrics/historical?days=7
```

### Access Dashboard
Open browser: `http://localhost:8082/index.html`

---

## 🔹 10. Important Observations

### Design Patterns Used

1. **Strategy Pattern** - `MetricsStorageStrategy` interface with multiple implementations (Timestream, S3)
2. **Dependency Injection** - Constructor-based injection throughout
3. **DTO Pattern** - Separation of API contracts from entities
4. **Repository Pattern** - JPA repositories for database access
5. **Connector Pattern** - Abstracted HTTP communication

### Strengths

✅ **Pluggable Storage** - Easy to switch between Timestream, S3, or no storage  
✅ **Graceful Degradation** - Works without AWS, falls back to in-memory  
✅ **Clean Separation** - Controllers → Services → Storage  
✅ **Lombok Integration** - Minimal boilerplate code  
✅ **Real-time Updates** - Dashboard polls every 10 seconds  
✅ **Prometheus Parsing** - Handles complex metric formats  

### Potential Improvements

⚠️ **No Authentication** - All endpoints are public  
⚠️ **Single In-Memory Cache** - AtomicReference lost on restart  
⚠️ **No Rate Limiting** - Kafka consumer can overwhelm the service  
⚠️ **Hardcoded Fallback** - MetricsParsingServiceHelper has static data  
⚠️ **Mixed Responsibilities** - Some controllers do too much  
⚠️ **No Validation** - POST /internal/metrics accepts any JSON  

### Risks

🔴 **Memory Leak** - AtomicReference grows unbounded if Kafka sends large payloads  
🔴 **AWS Costs** - Timestream can get expensive with high write volume  
🔴 **No Circuit Breaker** - External service failures can cascade  
🔴 **H2 Database** - In-memory, data lost on restart  

### Recommended Enhancements

1. Add Spring Security for authentication
2. Implement Redis for distributed caching
3. Add request validation with `@Valid`
4. Implement circuit breaker with Resilience4j
5. Add pagination for historical metrics
6. Implement WebSocket for real-time dashboard updates
7. Add metrics aggregation before storage (reduce Timestream writes)

---

## 🔹 11. Visual Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                        EXTERNAL SYSTEMS                              │
│                                                                       │
│  ┌──────────────────┐              ┌─────────────────────┐          │
│  │ Kafka Consumer   │              │ AI Agent Wrapper    │          │
│  │ (Port 8083)      │              │ (Port 8083)         │          │
│  └────────┬─────────┘              └──────────┬──────────┘          │
│           │                                    │                     │
└───────────┼────────────────────────────────────┼─────────────────────┘
            │ POST /internal/metrics             │ GET /ai/metrics
            │ (JSON payload)                     │ (Prometheus format)
            ▼                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT APPLICATION (Port 8082)               │
│                                                                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                    CONTROLLER LAYER                           │   │
│  │                                                               │   │
│  │  DashboardMetricsController    HistoricalMetricsController   │   │
│  │  ├─ POST /internal/metrics     ├─ GET /metrics/historical    │   │
│  │  └─ GET /internal/metrics      └─ GET /metrics/storage/status│   │
│  │                                                               │   │
│  │  AiMetricsController                                          │   │
│  │  └─ GET /ai/metrics/dashboard                                 │   │
│  └────────────────────┬──────────────────────┬───────────────────┘   │
│                       │                      │                       │
│                       ▼                      ▼                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                     SERVICE LAYER                             │   │
│  │                                                               │   │
│  │  MetricsStorageService         MetricsParsingService         │   │
│  │  ├─ storeMetrics()             ├─ parseMetrics()             │   │
│  │  ├─ getHistoricalMetrics()     └─ buildMetricTables()        │   │
│  │  └─ getActiveStorageType()                                   │   │
│  │                                                               │   │
│  │  AiMetricsService                                             │   │
│  │  └─ fetchRawMetrics()                                         │   │
│  └────────────────────┬──────────────────────┬───────────────────┘   │
│                       │                      │                       │
│                       ▼                      ▼                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │              INTEGRATION LAYER                                │   │
│  │                                                               │   │
│  │  AiMetricsConnector            Storage Strategies            │   │
│  │  ├─ get()                      ├─ TimestreamStorageStrategy  │   │
│  │  ├─ post()                     └─ S3StorageStrategy           │   │
│  │  ├─ put()                                                     │   │
│  │  └─ delete()                                                  │   │
│  └────────────────────┬──────────────────────┬───────────────────┘   │
│                       │                      │                       │
│                       ▼                      ▼                       │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                  PERSISTENCE LAYER                            │   │
│  │                                                               │   │
│  │  AtomicReference<Map>          H2 Database (JPA)             │   │
│  │  (In-Memory Cache)             (In-Memory)                   │   │
│  └───────────────────────────────────────────────────────────────┘   │
│                                                                       │
└───────────────────────────────────────────────────────────────────────┘
            │                                    │
            ▼                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         EXTERNAL STORAGE                             │
│                                                                       │
│  ┌──────────────────┐              ┌─────────────────────┐          │
│  │ AWS Timestream   │              │ AWS S3              │          │
│  │ (Time-series DB) │              │ (Object Storage)    │          │
│  └──────────────────┘              └─────────────────────┘          │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         FRONTEND DASHBOARD                           │
│                                                                       │
│  index.html (Chart.js)                                               │
│  ├─ Polls GET /internal/metrics every 10s                           │
│  ├─ Displays KPIs (Total Requests, Latency, Errors, Tokens)         │
│  ├─ Token Consumption Chart (Cyan input, Purple output)             │
│  ├─ Model Distribution Chart (Gemini, GPT-4, Claude)                │
│  └─ Latency Feed, Cost Analysis, Metric Tables                      │
│                                                                       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔹 12. Complete Request Flow Example

### Scenario: Kafka sends metrics → Dashboard displays

**Step 1:** Kafka consumer POSTs metrics
```bash
POST http://localhost:8082/internal/metrics
Content-Type: application/json

{
  "meta": {"kpis": [...]},
  "modelDistribution": [...],
  "latencyFeed": [...]
}
```

**Step 2:** `DashboardMetricsController.receiveMetrics()` executes
- Stores payload in `AtomicReference<Map<String, Object>>`
- Calls `MetricsStorageService.storeMetrics(payload)`

**Step 3:** `MetricsStorageService` routes to active strategy
- If `metrics.storage.type=timestream` → `TimestreamStorageStrategy.store()`
- If `metrics.storage.type=s3` → `S3StorageStrategy.store()`
- If not configured → logs "Storage disabled"

**Step 4:** `TimestreamStorageStrategy.store()` writes to AWS
- Extracts 4 KPIs from `meta.kpis`
- Extracts 2 model distributions from `modelDistribution`
- Calculates 1 latency average from `latencyFeed`
- Writes 7 records to Timestream

**Step 5:** Dashboard polls for updates
```bash
GET http://localhost:8082/internal/metrics
```

**Step 6:** `DashboardMetricsController.fetchMetrics()` returns cached data
- Returns `AtomicReference.get()` (latest metrics from Kafka)

**Step 7:** Frontend updates charts
- Parses JSON response
- Updates KPI cards
- Redraws Chart.js charts
- Displays latency feed

---

## 🔹 13. Key Endpoints Summary

| Endpoint | Method | Purpose | Response |
|----------|--------|---------|----------|
| `/internal/metrics` | POST | Receive metrics from Kafka | 200 OK |
| `/internal/metrics` | GET | Get current metrics for dashboard | JSON (KPIs, charts) |
| `/metrics/historical` | GET | Get historical metrics | List of records |
| `/metrics/storage/status` | GET | Check storage configuration | {enabled, type} |
| `/metrics/store` | POST | Manually store metrics | 200 OK |
| `/ai/metrics/dashboard` | GET | Fetch & parse from AI wrapper | MetricsResponseDTO |
| `/observe/health` | GET | Health check | UP/DOWN |
| `/observe/metrics` | GET | Actuator metrics | Prometheus format |

---

## 🔹 14. Dependencies Summary

### Core Dependencies
- **Spring Boot Starter Web** - REST API
- **Spring Boot Starter WebFlux** - WebClient for HTTP
- **Spring Boot Starter Data JPA** - Database access
- **Spring Boot Starter Actuator** - Health checks
- **H2 Database** - In-memory database
- **Lombok** - Reduce boilerplate

### AWS Dependencies
- **AWS SDK Timestream Write** - Write metrics to Timestream
- **AWS SDK Timestream Query** - Query historical data
- **AWS SDK S3** - Store JSON payloads

### Build Tools
- **Maven** - Build and dependency management
- **Maven Compiler Plugin** - Lombok annotation processing

---

## 🔹 15. Environment Variables (Optional)

```bash
# AWS Credentials (if not using aws configure)
export AWS_ACCESS_KEY_ID=your_key
export AWS_SECRET_ACCESS_KEY=your_secret
export AWS_REGION=us-east-1

# Override application properties
export SERVER_PORT=8082
export WRAPPER_SERVICE_URL=http://localhost:8083/ai/metrics
export METRICS_STORAGE_TYPE=timestream
export TIMESTREAM_DATABASE=ai_metrics_db
export TIMESTREAM_TABLE=metrics_table
```

---

## 🔹 16. Troubleshooting Guide

### Issue: Application fails to start
**Cause:** Missing MetricsStorageStrategy bean  
**Solution:** Ensure `metrics.storage.type` is commented out or set to valid value

### Issue: No metrics in dashboard
**Cause:** Kafka consumer not sending data  
**Solution:** Test manually with curl POST to `/internal/metrics`

### Issue: Timestream write fails
**Cause:** AWS credentials not configured  
**Solution:** Run `aws configure` or set environment variables

### Issue: Dashboard shows stale data
**Cause:** Kafka consumer stopped or network issue  
**Solution:** Check Kafka consumer logs, verify network connectivity

---

## 🔹 17. Related Documentation

- `KAFKA_TIMESTREAM_FLOW.md` - Detailed flow from Kafka to Timestream
- `STORAGE_IMPLEMENTATION.md` - Storage architecture and setup
- `QUICK_START.md` - Quick start guide without Timestream
- `README.md` - Basic project information

---

**Document Version:** 1.0  
**Last Updated:** 2024  
**Maintained By:** VM Technology Solution Center
