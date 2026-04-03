# VM Service Claims Review - Metrics Dashboard Backend

## 🔹 Project Overview

**Type**: Spring Boot 3.5.7 REST API Microservice  
**Language**: Java 17  
**Build Tool**: Maven  
**Purpose**: Real-time metrics processing and storage service for AI/ML claims review system

This application receives metrics data (from Kafka consumers or external sources), validates it, caches it for real-time dashboard access, and optionally persists it to AWS storage backends (Timestream or S3) for historical analysis.

---

## 🔹 Entry Point

**Main Class**: `ClaimsreviewApplication.java`

```java
@SpringBootApplication
public class ClaimsreviewApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClaimsreviewApplication.class, args);
    }
}
```

**How it starts**:
1. Spring Boot auto-configuration scans `com.vm.service.claimsreview` package
2. Loads configuration from `application.properties`
3. Initializes beans based on conditional properties (AWS clients, storage strategies)
4. Starts embedded Tomcat server on port **8082**
5. Exposes REST endpoints for metrics operations

---

## 🔹 High-Level Architecture

**Architecture Pattern**: Layered Architecture with Strategy Pattern

```
┌─────────────────────────────────────────────────────────┐
│                    Controllers Layer                     │
│  (HTTP Request Handling & Response Mapping)             │
└─────────────────────────────────────────────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│                    Service Layer                         │
│  (Business Logic, Validation, Orchestration)            │
└─────────────────────────────────────────────────────────┘
                          ↓
┌──────────────────┬──────────────────┬───────────────────┐
│   Cache Layer    │  Storage Layer   │  Validation Layer │
│   (In-Memory)    │  (Strategy)      │  (Rules)          │
└──────────────────┴──────────────────┴───────────────────┘
                          ↓
┌─────────────────────────────────────────────────────────┐
│              Storage Implementations                     │
│  (Timestream / S3 / None - Strategy Pattern)           │
└─────────────────────────────────────────────────────────┘
```

**Major Layers**:
- **Controllers**: REST endpoints (`DashboardMetricsController`, `HistoricalMetricsController`, `HealthController`)
- **Services**: Business logic (`DashboardMetricsService`, `HistoricalMetricsService`, `MetricsStorageService`, `MetricsValidationService`)
- **Cache**: In-memory storage (`MetricsCacheService`)
- **Storage**: Pluggable persistence strategies (`TimestreamStorageStrategy`, `S3StorageStrategy`)
- **Configuration**: AWS clients and CORS setup (`AwsStorageConfig`, `CorsConfig`, `WebClientConfig`)

---

## 🔹 End-to-End Flow (CRITICAL)

### **Flow 1: Receiving Metrics from Kafka/External Source**

```
Kafka Consumer / External System
         ↓
    POST /internal/metrics
         ↓
[DashboardMetricsController.receiveMetrics()]
         ↓
[DashboardMetricsService.processIncomingMetrics()]
         ↓
    ┌────┴────┐
    ↓         ↓
[MetricsValidationService.validate()]
    ↓
[MetricsCacheService.store()]  ← Stores in AtomicReference (in-memory)
    ↓
[MetricsStorageService.storeMetrics()]
    ↓
[TimestreamStorageStrategy.store() OR S3StorageStrategy.store()]
    ↓
AWS Timestream/S3 (if enabled)
    ↓
Response: 202 ACCEPTED
```

**Actual Files Involved**:
1. `DashboardMetricsController.java` - Receives POST request
2. `DashboardMetricsService.java` - Orchestrates processing
3. `MetricsValidationService.java` - Validates payload structure and size
4. `MetricsCacheService.java` - Stores in memory using `AtomicReference`
5. `MetricsStorageService.java` - Delegates to storage strategy
6. `TimestreamStorageStrategy.java` or `S3StorageStrategy.java` - Persists to AWS
7. `ServiceResponse.java` - Wraps response with status

---

### **Flow 2: Dashboard Fetching Current Metrics**

```
Dashboard UI / Frontend
         ↓
    GET /internal/metrics
         ↓
[DashboardMetricsController.fetchMetrics()]
         ↓
[DashboardMetricsService.getCurrentMetrics()]
         ↓
[MetricsCacheService.retrieve()]
         ↓
Returns cached metrics (or "No metrics available")
         ↓
Response: 200 OK with JSON data
```

**Actual Files Involved**:
1. `DashboardMetricsController.java` - Handles GET request
2. `DashboardMetricsService.java` - Retrieves from cache
3. `MetricsCacheService.java` - Returns cached data from `AtomicReference`
4. `ServiceResponse.java` - Wraps response

---

### **Flow 3: Fetching Historical Metrics**

```
Dashboard UI / Admin
         ↓
    GET /metrics/historical?days=7
         ↓
[HistoricalMetricsController.getHistoricalMetrics()]
         ↓
[HistoricalMetricsService.getHistoricalMetrics()]
         ↓
[MetricsStorageService.getHistoricalMetrics()]
         ↓
[TimestreamStorageStrategy.retrieve() OR S3StorageStrategy.retrieve()]
         ↓
AWS Timestream Query / S3 List & Get
         ↓
Response: 200 OK with List<Map<String, Object>>
```

**Actual Files Involved**:
1. `HistoricalMetricsController.java` - Handles GET request with days parameter
2. `HistoricalMetricsService.java` - Validates days parameter (1-365)
3. `MetricsStorageService.java` - Delegates to storage strategy
4. `TimestreamStorageStrategy.java` or `S3StorageStrategy.java` - Queries AWS
5. `ServiceResponse.java` - Wraps response

---

## 🔹 Key Components & Their Responsibilities

### **Controllers** (`controllers/`)

| File | Responsibility | Endpoints |
|------|---------------|-----------|
| `DashboardMetricsController.java` | Handles real-time metrics POST/GET | `POST /internal/metrics`<br>`GET /internal/metrics` |
| `HistoricalMetricsController.java` | Handles historical data queries | `GET /metrics/historical?days=N`<br>`GET /metrics/storage/status`<br>`POST /metrics/store` |
| `HealthController.java` | Health check endpoint | `GET /claimsreview/health` |

---

### **Services** (`service/`)

| File | Responsibility |
|------|---------------|
| `DashboardMetricsService.java` | Orchestrates metrics processing: validation → cache → storage |
| `HistoricalMetricsService.java` | Handles historical data retrieval and manual storage |
| `MetricsStorageService.java` | Manages storage strategy selection and delegation |
| `MetricsValidationService.java` | Validates metrics payload (null check, size limit, structure) |

---

### **Cache** (`cache/`)

| File | Responsibility |
|------|---------------|
| `MetricsCacheService.java` | In-memory cache using `AtomicReference<Map<String, Object>>` for thread-safe real-time access |

---

### **Storage** (`storage/`)

| File | Responsibility | When Active |
|------|---------------|-------------|
| `MetricsStorageStrategy.java` | Interface defining storage contract | Always (interface) |
| `TimestreamStorageStrategy.java` | AWS Timestream time-series database storage | `metrics.storage.type=timestream` |
| `S3StorageStrategy.java` | AWS S3 object storage for archival | `metrics.storage.type=s3` |

---

### **Configuration** (`configrs/`)

| File | Responsibility |
|------|---------------|
| `AwsStorageConfig.java` | Conditionally creates AWS SDK clients (Timestream, S3) based on properties |
| `CorsConfig.java` | Enables CORS for all origins (GET method) |
| `WebClientConfig.java` | Configures reactive WebClient bean |

---

### **Response & Constants**

| File | Responsibility |
|------|---------------|
| `ServiceResponse.java` | Generic wrapper for service responses (success/error, HTTP status) |
| `MetricsConstants.java` | Centralized constants for JSON keys, storage types, queries |

---

## 🔹 Data Flow

### **Metrics Payload Structure** (JSON)

```json
{
  "meta": {
    "kpis": [
      {"id": "total_requests", "value": "3", "title": "Total Requests"},
      {"id": "avg_latency", "value": "1590", "title": "Avg Latency (ms)"},
      {"id": "error_rate", "value": "33.0%", "title": "Error Rate"},
      {"id": "total_tokens", "value": "512", "title": "Total Tokens"}
    ]
  },
  "modelDistribution": [
    {"model": "gemini", "percentage": 67},
    {"model": "openai", "percentage": 33}
  ],
  "latencyFeed": [2499.0, 1206.0, 1448.0]
}
```

### **Data Transformation**

1. **Validation**: Checks payload is non-null, non-empty, size < 1MB
2. **Caching**: Stores entire JSON as-is in `AtomicReference`
3. **Storage Transformation** (Timestream):
   - Extracts 4 KPI records (total_requests, avg_latency, error_rate, total_tokens)
   - Extracts 2 model distribution records (model_gemini, model_openai)
   - Calculates 1 latency average record (latency_feed_avg)
   - **Total: 7 Timestream records per POST**

---

## 🔹 External Integrations

### **1. AWS Timestream** (Time-Series Database)
- **Purpose**: Store metrics with timestamps for historical analysis
- **Configuration**: `metrics.storage.type=timestream`
- **Required Properties**:
  - `aws.region` (default: us-east-1)
  - `timestream.database` (e.g., ai_metrics_db)
  - `timestream.table` (e.g., metrics_table)
- **SDK**: `software.amazon.awssdk:timestreamwrite`, `software.amazon.awssdk:timestreamquery`

### **2. AWS S3** (Object Storage)
- **Purpose**: Archive metrics as JSON files for long-term storage
- **Configuration**: `metrics.storage.type=s3`
- **Required Properties**:
  - `aws.region`
  - `s3.bucket.name`
- **SDK**: `software.amazon.awssdk:s3`

### **3. Kafka** (Message Queue - External)
- **Purpose**: Kafka consumer sends metrics to this service
- **Integration Point**: `POST /internal/metrics`
- **Note**: Kafka consumer is external to this service

### **4. H2 Database** (In-Memory)
- **Purpose**: Embedded database (currently unused, but configured)
- **Configuration**: `spring.datasource.url=jdbc:h2:mem:testdb`
- **Console**: Enabled at `/h2-console`

---

## 🔹 Configuration & Setup

### **application.properties**

```properties
# Application
spring.application.name=claimsreview
server.port=8082

# H2 Database (embedded, currently unused)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# External Service URL (if needed)
wrapper.service.url=http://localhost:8083/ai/metrics

# AWS Timestream Storage (UNCOMMENT TO ENABLE)
#metrics.storage.type=timestream
#aws.region=us-east-1
#timestream.database=ai_metrics_db
#timestream.table=metrics_table

# AWS S3 Storage (ALTERNATIVE TO TIMESTREAM)
#metrics.storage.type=s3
#aws.region=us-east-1
#s3.bucket.name=my-metrics-bucket
```

### **Environment Variables**

- `PORT`: Server port (default: 8082)
- `AWS_ACCESS_KEY_ID`: AWS credentials
- `AWS_SECRET_ACCESS_KEY`: AWS credentials
- `AWS_REGION`: AWS region (default: us-east-1)

---

## 🔹 Build & Run Instructions

### **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- (Optional) AWS CLI configured with credentials

### **Step 1: Clone Repository**
```bash
git clone <repository-url>
cd vm-service-claims-review
```

### **Step 2: Build Project**
```bash
mvn clean install
```

### **Step 3: Run Application**

**Option A: Using Maven**
```bash
mvn spring-boot:run
```

**Option B: Using JAR**
```bash
java -jar target/claimsreview-0.0.1-SNAPSHOT.jar
```

**Option C: Using Docker**
```bash
docker build -t claims-review-service .
docker run -p 8082:8082 claims-review-service
```

### **Step 4: Verify Application**
```bash
curl http://localhost:8082/claimsreview/health
```

Expected Response:
```json
{
  "status": "UP",
  "message": "Service is running"
}
```

---

## 🔹 API Endpoints

### **Health Check**
```http
GET /claimsreview/health
```

### **Receive Metrics (from Kafka/External)**
```http
POST /internal/metrics
Content-Type: application/json

{
  "meta": {...},
  "modelDistribution": [...],
  "latencyFeed": [...]
}
```

### **Get Current Metrics (for Dashboard)**
```http
GET /internal/metrics
```

### **Get Historical Metrics**
```http
GET /metrics/historical?days=7
```
Query Parameters:
- `days` (optional, default: 7, range: 1-365)

### **Get Storage Status**
```http
GET /metrics/storage/status
```

Response:
```json
{
  "enabled": true,
  "type": "TIMESTREAM"
}
```

### **Manual Store Metrics (Admin/Testing)**
```http
POST /metrics/store
Content-Type: application/json

{
  "meta": {...},
  "modelDistribution": [...],
  "latencyFeed": [...]
}
```

---

## 🔹 Important Observations

### **Design Patterns Used**

1. **Strategy Pattern**: `MetricsStorageStrategy` interface with multiple implementations (Timestream, S3)
2. **Dependency Injection**: All services use constructor injection with `@AllArgsConstructor`
3. **Single Responsibility Principle**: Each service has one clear purpose
4. **Open/Closed Principle**: New storage strategies can be added without modifying existing code
5. **Conditional Bean Creation**: AWS clients created only when needed (`@ConditionalOnProperty`)

### **Strengths**

✅ Clean separation of concerns (Controller → Service → Storage)  
✅ Thread-safe caching with `AtomicReference`  
✅ Flexible storage backends (Timestream, S3, or none)  
✅ Comprehensive error handling with `ServiceResponse`  
✅ Centralized constants in `MetricsConstants`  
✅ Lombok reduces boilerplate code  
✅ Docker support for containerization  

### **Potential Improvements**

⚠️ **No Kafka Consumer**: Currently relies on external Kafka consumer to POST data  
⚠️ **No Authentication**: Endpoints are open (consider adding Spring Security)  
⚠️ **CORS Wide Open**: `allowedOrigins("*")` should be restricted in production  
⚠️ **H2 Database Unused**: JPA dependencies included but not utilized  
⚠️ **No Rate Limiting**: High-frequency POSTs could overwhelm the service  
⚠️ **Cache Eviction**: No TTL or size limit on in-memory cache  
⚠️ **No Metrics Aggregation**: Stores raw data without aggregation logic  
⚠️ **Error Handling**: Could benefit from global exception handler (`@ControllerAdvice`)  

### **Risks**

🔴 **Memory Leak**: `AtomicReference` cache grows indefinitely without eviction  
🔴 **Data Loss**: In-memory cache lost on restart (no persistence fallback)  
🔴 **AWS Costs**: Timestream writes can be expensive at high volume  
🔴 **No Monitoring**: Missing metrics/observability (consider Spring Actuator endpoints)  

---

## 🔹 Visual Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                    EXTERNAL SYSTEMS                              │
│  Kafka Consumer / External Service / Dashboard UI               │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                              │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐  │
│  │ Dashboard        │  │ Historical       │  │ Health       │  │
│  │ Metrics          │  │ Metrics          │  │ Controller   │  │
│  │ Controller       │  │ Controller       │  │              │  │
│  └──────────────────┘  └──────────────────┘  └──────────────┘  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                    SERVICE LAYER                                 │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────┐  │
│  │ Dashboard        │  │ Historical       │  │ Validation   │  │
│  │ Metrics          │  │ Metrics          │  │ Service      │  │
│  │ Service          │  │ Service          │  │              │  │
│  └──────────────────┘  └──────────────────┘  └──────────────┘  │
│           │                      │                               │
│           └──────────┬───────────┘                               │
│                      ↓                                           │
│           ┌──────────────────────┐                               │
│           │ Metrics Storage      │                               │
│           │ Service              │                               │
│           └──────────────────────┘                               │
└────────────────────────┬────────────────────────────────────────┘
                         │
         ┌───────────────┼───────────────┐
         ↓               ↓               ↓
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│   Cache     │  │  Timestream │  │     S3      │
│  (Memory)   │  │  Strategy   │  │  Strategy   │
│ AtomicRef   │  │             │  │             │
└─────────────┘  └─────────────┘  └─────────────┘
                         │               │
                         ↓               ↓
                 ┌─────────────────────────┐
                 │      AWS SERVICES       │
                 │  Timestream  /  S3      │
                 └─────────────────────────┘
```

---

## 🔹 Testing

### **Manual Testing**

**1. Test Health Endpoint**
```bash
curl http://localhost:8082/claimsreview/health
```

**2. Post Sample Metrics**
```bash
curl -X POST http://localhost:8082/internal/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "meta": {
      "kpis": [
        {"id": "total_requests", "value": "100", "title": "Total Requests"}
      ]
    },
    "modelDistribution": [
      {"model": "gemini", "percentage": 60}
    ],
    "latencyFeed": [1500.0, 1600.0]
  }'
```

**3. Fetch Current Metrics**
```bash
curl http://localhost:8082/internal/metrics
```

**4. Get Historical Metrics**
```bash
curl http://localhost:8082/metrics/historical?days=7
```

**5. Check Storage Status**
```bash
curl http://localhost:8082/metrics/storage/status
```

### **Unit Tests**
```bash
mvn test
```

---

## 🔹 AWS Setup (Optional)

### **Enable Timestream Storage**

**1. Create Timestream Database**
```bash
aws timestream-write create-database --database-name ai_metrics_db
```

**2. Create Timestream Table**
```bash
aws timestream-write create-table \
  --database-name ai_metrics_db \
  --table-name metrics_table \
  --retention-properties MemoryStoreRetentionPeriodInHours=24,MagneticStoreRetentionPeriodInDays=90
```

**3. Update application.properties**
```properties
metrics.storage.type=timestream
aws.region=us-east-1
timestream.database=ai_metrics_db
timestream.table=metrics_table
```

**4. Configure AWS Credentials**
```bash
aws configure
# Enter AWS Access Key ID
# Enter AWS Secret Access Key
# Enter region: us-east-1
```

---

## 🔹 Troubleshooting

### **Application won't start**
- Check Java version: `java -version` (must be 17+)
- Check port 8082 is available: `netstat -an | findstr 8082`
- Check logs in console for errors

### **No metrics returned**
- Ensure metrics have been POSTed to `/internal/metrics`
- Check cache has data: `GET /metrics/storage/status`
- Verify Kafka consumer is running and POSTing

### **Timestream errors**
- Verify AWS credentials: `aws sts get-caller-identity`
- Check database exists: `aws timestream-write describe-database --database-name ai_metrics_db`
- Check table exists: `aws timestream-write describe-table --database-name ai_metrics_db --table-name metrics_table`

### **CORS errors**
- Check `CorsConfig.java` allows your origin
- Verify browser console for CORS errors

---

## 🔹 Project Structure

```
vm-service-claims-review/
├── src/
│   ├── main/
│   │   ├── java/com/vm/service/claimsreview/
│   │   │   ├── cache/
│   │   │   │   └── MetricsCacheService.java
│   │   │   ├── configrs/
│   │   │   │   ├── AwsStorageConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   └── WebClientConfig.java
│   │   │   ├── constants/
│   │   │   │   └── MetricsConstants.java
│   │   │   ├── controllers/
│   │   │   │   ├── DashboardMetricsController.java
│   │   │   │   ├── HealthController.java
│   │   │   │   └── HistoricalMetricsController.java
│   │   │   ├── response/
│   │   │   │   └── ServiceResponse.java
│   │   │   ├── service/
│   │   │   │   ├── DashboardMetricsService.java
│   │   │   │   ├── HistoricalMetricsService.java
│   │   │   │   ├── MetricsStorageService.java
│   │   │   │   └── MetricsValidationService.java
│   │   │   ├── storage/
│   │   │   │   ├── MetricsStorageStrategy.java
│   │   │   │   ├── S3StorageStrategy.java
│   │   │   │   └── TimestreamStorageStrategy.java
│   │   │   └── ClaimsreviewApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   │           └── index.html
│   └── test/
│       └── java/com/vm/service/claimsreview/
│           └── ClaimsreviewApplicationTests.java
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
├── KAFKA_TIMESTREAM_FLOW.md
└── README.md
```

---

## 🔹 Dependencies (Key Libraries)

- **Spring Boot 3.5.7**: Core framework
- **Spring Web**: REST API support
- **Spring WebFlux**: Reactive WebClient
- **Spring Data JPA**: Database abstraction
- **Spring Actuator**: Health checks and monitoring
- **Lombok**: Reduces boilerplate code
- **H2 Database**: Embedded in-memory database
- **AWS SDK Timestream**: Time-series database client
- **AWS SDK S3**: Object storage client
- **Jackson**: JSON serialization/deserialization

---

## 🔹 License & Contact

**Developer**: Srivani  
**Version**: 0.0.1-SNAPSHOT  
**License**: Not specified

---

## 🔹 Quick Reference

| Task | Command |
|------|---------|
| Build | `mvn clean install` |
| Run | `mvn spring-boot:run` |
| Test | `mvn test` |
| Package | `mvn package` |
| Docker Build | `docker build -t claims-review .` |
| Docker Run | `docker run -p 8082:8082 claims-review` |
| Health Check | `curl http://localhost:8082/claimsreview/health` |

---

**Last Updated**: 2025  
**Spring Boot Version**: 3.5.7  
**Java Version**: 17
