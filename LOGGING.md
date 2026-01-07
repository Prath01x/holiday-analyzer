# Logging Guide

This document explains the logging implementation for the Holiday Analyzer application.

## 12-Factor App Compliance

This application follows **Factor XI: Logs** of the [12-Factor App](https://12factor.net/logs) methodology:
- Logs are treated as **event streams**
- Application writes all logs to **stdout** (not files)
- Logs are captured by the execution environment (Docker, Kubernetes, Cloud)
- Structured logging in production (JSON format)
- Different log levels per environment

---

## Logging Architecture

### Development Environment
- **Format:** Human-readable text
- **Level:** DEBUG for application, INFO for root
- **Output:** Console (stdout)
- **SQL Queries:** Visible for debugging

### Production Environment
- **Format:** Structured JSON (Logstash format)
- **Level:** INFO for application, WARN for root
- **Output:** Console (stdout) → captured by container orchestration
- **SQL Queries:** Hidden for performance

---

## Log Levels

| Level | Usage | Example |
|-------|-------|---------|
| **ERROR** | System errors, exceptions | Database connection failed |
| **WARN** | Warning conditions | Deprecated API usage |
| **INFO** | Important events | Application started, User logged in |
| **DEBUG** | Detailed information | Request parameters, Query results |
| **TRACE** | Very detailed | Method entry/exit |

---

## Configuration

### Logback Configuration
Location: `backend/src/main/resources/logback-spring.xml`

**Features:**
- Profile-specific logging (dev, prod, test)
- Console output (Docker/Cloud friendly)
- JSON structured logging in production
- MDC (Mapped Diagnostic Context) support for correlation IDs

### Profile-Specific Logging

#### Development Profile
```xml
<springProfile name="dev">
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
    </root>
    <logger name="com.holidayanalyzer" level="DEBUG"/>
</springProfile>
```

#### Production Profile
```xml
<springProfile name="prod">
    <root level="WARN">
        <appender-ref ref="JSON_CONSOLE"/>
    </root>
    <logger name="com.holidayanalyzer" level="INFO"/>
</springProfile>
```

---

## Using Logging in Code

### Basic Logging

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HolidayController {
    private static final Logger log = LoggerFactory.getLogger(HolidayController.class);
    
    @GetMapping("/api/holidays")
    public List<Holiday> getHolidays() {
        log.info("Fetching holidays");
        
        try {
            List<Holiday> holidays = holidayService.findAll();
            log.debug("Found {} holidays", holidays.size());
            return holidays;
        } catch (Exception e) {
            log.error("Error fetching holidays", e);
            throw e;
        }
    }
}
```

### Structured Logging with MDC

```java
import org.slf4j.MDC;

public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                   HttpServletResponse response, 
                                   FilterChain filterChain) {
        // Add correlation ID to all logs
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        
        try {
            log.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(request, response);
            log.info("Request completed with status: {}", response.getStatus());
        } finally {
            MDC.clear();
        }
    }
}
```

---

## Log Output Examples

### Development (Human-Readable)
```
2026-01-07 16:00:00.123 [http-nio-8080-exec-1] INFO  c.h.controller.HolidayController - Fetching holidays
2026-01-07 16:00:00.456 [http-nio-8080-exec-1] DEBUG c.h.controller.HolidayController - Found 150 holidays
2026-01-07 16:00:01.789 [http-nio-8080-exec-2] ERROR c.h.service.HolidayService - Error fetching holidays
java.sql.SQLException: Connection timeout
    at org.postgresql.jdbc.PgConnection.connect(PgConnection.java:123)
```

### Production (JSON)
```json
{
  "@timestamp": "2026-01-07T16:00:00.123Z",
  "level": "INFO",
  "logger_name": "com.holidayanalyzer.controller.HolidayController",
  "message": "Fetching holidays",
  "thread_name": "http-nio-8080-exec-1",
  "correlationId": "abc123-def456-ghi789",
  "application": "holiday-analyzer-backend",
  "environment": "production"
}
```

---

## Viewing Logs

### Local Development
```bash
# Backend logs
cd backend
mvn spring-boot:run
# Logs appear in console
```

### Docker
```bash
# View all logs
docker-compose logs -f

# View backend logs only
docker-compose logs -f backend

# View last 100 lines
docker-compose logs --tail=100 backend

# Follow logs in real-time
docker logs -f backend
```

### Kubernetes
```bash
# View pod logs
kubectl logs -f deployment/holiday-analyzer-backend

# View logs from all pods
kubectl logs -f -l app=holiday-analyzer-backend

# View logs with timestamps
kubectl logs --timestamps deployment/holiday-analyzer-backend
```

### Cloud Platforms

**AWS CloudWatch:**
```bash
# Logs automatically sent to CloudWatch Logs
# View in AWS Console → CloudWatch → Log Groups
```

**Google Cloud Logging:**
```bash
# View logs
gcloud logging read "resource.type=cloud_run_revision"

# Stream logs
gcloud logging tail
```

**Azure Monitor:**
```bash
# View in Azure Portal → Monitor → Logs
# Or use CLI
az monitor log-analytics query
```

---

## Log Aggregation

### ELK Stack (Elasticsearch, Logstash, Kibana)

**docker-compose.yml addition:**
```yaml
elasticsearch:
  image: elasticsearch:8.11.0
  environment:
    - discovery.type=single-node
  ports:
    - "9200:9200"

kibana:
  image: kibana:8.11.0
  ports:
    - "5601:5601"
  depends_on:
    - elasticsearch
```

**Logstash configuration:**
```conf
input {
  tcp {
    port => 5000
    codec => json
  }
}

output {
  elasticsearch {
    hosts => ["elasticsearch:9200"]
    index => "holiday-analyzer-%{+YYYY.MM.dd}"
  }
}
```

---

## Best Practices

### ✅ DO:
- Use appropriate log levels (INFO for important events, DEBUG for details)
- Include context in log messages (user ID, request ID, etc.)
- Log exceptions with stack traces: `log.error("Error", exception)`
- Use structured logging in production (JSON)
- Log to stdout/stderr only (not files)
- Use correlation IDs to track requests

### ❌ DON'T:
- Log sensitive information (passwords, tokens, credit cards)
- Log too much in production (performance impact)
- Use `System.out.println()` (use logger instead)
- Log to files (use stdout for cloud compatibility)
- Include PII (Personally Identifiable Information) without masking

---

## Troubleshooting

### Problem: No logs appearing
**Solution:** Check `SPRING_PROFILES_ACTIVE` is set correctly

### Problem: Too many logs in production
**Solution:** Increase log level to WARN or ERROR in `application-prod.properties`

### Problem: Can't find specific log entry
**Solution:** Use correlation IDs and structured logging (JSON) for better searchability

### Problem: Logs not in JSON format
**Solution:** Ensure `SPRING_PROFILES_ACTIVE=prod` is set

---

## Performance Considerations

### Log Level Impact
- **DEBUG:** High overhead, use only in development
- **INFO:** Moderate overhead, acceptable in production
- **WARN/ERROR:** Low overhead, recommended for production

### Async Logging (Optional)
For high-throughput applications, enable async logging:

```xml
<appender name="ASYNC" class="ch.qos.logback.classic.AsyncAppender">
    <appender-ref ref="JSON_CONSOLE"/>
    <queueSize>512</queueSize>
</appender>
```

---

## Monitoring and Alerts

### Key Metrics to Monitor
- Error rate (ERROR level logs)
- Response time (from request logs)
- Exception frequency
- Database query performance

### Setting Up Alerts
```yaml
# Example: Alert on high error rate
alert: HighErrorRate
expr: rate(log_messages{level="ERROR"}[5m]) > 10
for: 5m
annotations:
  summary: "High error rate detected"
```

---

## Additional Resources

- [12-Factor App: Logs](https://12factor.net/logs)
- [Logback Documentation](https://logback.qos.ch/documentation.html)
- [SLF4J Manual](http://www.slf4j.org/manual.html)
- [Logstash Encoder](https://github.com/logfellow/logstash-logback-encoder)
