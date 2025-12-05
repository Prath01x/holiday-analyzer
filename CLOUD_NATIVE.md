# Cloud-Native Architecture & 12-Factor App Compliance

## 📋 12-Factor App Principles

### ✅ Already Implemented

1. **I. Codebase** ✅
   - Single codebase tracked in Git
   - Multiple deployments (local, Docker, GCP)

2. **III. Config** ✅
   - Configuration in environment variables
   - `application.properties` uses env vars in Docker
   - Database credentials externalized

3. **V. Build, Release, Run** ✅
   - Separate build stage in Dockerfiles
   - Multi-stage builds for backend and frontend
   - Clear separation of concerns

4. **VI. Processes** ✅
   - Stateless application design
   - Database handles persistent data
   - No local file storage for state

5. **VIII. Concurrency** ✅
   - Spring Boot handles concurrent requests
   - Horizontally scalable design
   - Docker containers for process isolation

6. **IX. Disposability** ✅
   - Fast startup with Spring Boot
   - Graceful shutdown built-in
   - Docker containers are disposable

### ⏳ To Be Implemented

7. **II. Dependencies** ⏳
   - Backend: Maven manages dependencies ✅
   - Frontend: npm manages dependencies ✅
   - **TODO**: Explicitly declare all system dependencies

8. **IV. Backing Services** ⏳
   - PostgreSQL as attached resource ✅
   - **TODO**: Add external holiday APIs as backing services
   - **TODO**: Use connection strings from environment

9. **VII. Port Binding** ⏳
   - Backend exports HTTP on port 8080 ✅
   - Frontend exports HTTP on port 80 ✅
   - **TODO**: Make ports configurable via env vars

10. **X. Dev/Prod Parity** ⏳
    - Docker ensures environment consistency ✅
    - **TODO**: Use same database (PostgreSQL) in all environments
    - **TODO**: Minimize time gap between deployments

11. **XI. Logs** ⏳
    - **TODO**: Stream logs to stdout/stderr
    - **TODO**: Integrate with GCP Cloud Logging
    - **TODO**: Structured logging (JSON format)

12. **XII. Admin Processes** ⏳
    - **TODO**: Database migrations as one-off processes
    - **TODO**: Seed data scripts
    - **TODO**: Admin tasks via separate containers

---

## 🚀 Google Cloud Platform Deployment Plan

### Phase 1: Container Registry
```bash
# Build and push images to GCP Container Registry
gcloud builds submit --tag gcr.io/PROJECT_ID/holiday-analyzer-backend
gcloud builds submit --tag gcr.io/PROJECT_ID/holiday-analyzer-frontend
```

### Phase 2: Cloud SQL (PostgreSQL)
- Create Cloud SQL PostgreSQL instance
- Configure private IP for security
- Set up automated backups
- Connection via Cloud SQL Proxy

### Phase 3: Google Kubernetes Engine (GKE)
- Create GKE cluster
- Deploy backend and frontend as separate services
- Configure ingress for external access
- Set up horizontal pod autoscaling

### Phase 4: CI/CD with Cloud Build
- Automated builds on git push
- Run tests before deployment
- Deploy to staging → production

---

## 📁 Required GCP Resources

### Infrastructure
- **GKE Cluster**: Standard cluster with 3 nodes
- **Cloud SQL**: PostgreSQL 15 instance
- **Container Registry**: Store Docker images
- **Cloud Storage**: Static assets (if needed)
- **Cloud Load Balancer**: Traffic distribution
- **Cloud Logging**: Centralized logs
- **Cloud Monitoring**: Metrics and alerts

### Networking
- VPC with private subnets
- Cloud NAT for outbound traffic
- Firewall rules for security

---

## 🔧 Configuration Management

### Environment Variables (Per Environment)

**Development (Local):**
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/holidays
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

**Production (GCP):**
```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://CLOUD_SQL_IP:5432/holidays
SPRING_DATASOURCE_USERNAME=${DB_USER}
SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
HOLIDAY_API_KEY=${API_KEY}
```

### Kubernetes Secrets
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: holiday-analyzer-secrets
type: Opaque
data:
  db-password: <base64-encoded>
  api-key: <base64-encoded>
```

---

## 📊 Observability Strategy

### Logging
- **Local**: Console output
- **GCP**: Cloud Logging with structured JSON
- **Format**: Timestamp, level, service, message, trace ID

### Monitoring
- **Metrics**: Request rate, error rate, latency
- **Health Checks**: `/actuator/health` endpoint
- **Alerts**: High error rate, slow response time

### Tracing
- **Cloud Trace**: Distributed tracing
- **Spring Cloud Sleuth**: Request correlation

---

## 🔐 Security Best Practices

### Implemented
- ✅ No hardcoded credentials
- ✅ Environment-based configuration
- ✅ .gitignore for sensitive files

### To Implement
- ⏳ Secret Manager for API keys
- ⏳ IAM roles and service accounts
- ⏳ HTTPS/TLS certificates
- ⏳ Network policies in Kubernetes
- ⏳ Container vulnerability scanning

---

## 📝 Next Steps for Cloud Deployment

### Immediate (Before First Feature)
1. ✅ Project structure setup
2. ⏳ Add health check endpoints
3. ⏳ Configure structured logging
4. ⏳ Add Kubernetes manifests

### Short Term (With Features)
1. ⏳ Implement database migrations (Flyway/Liquibase)
2. ⏳ Add integration tests
3. ⏳ Set up Cloud Build CI/CD
4. ⏳ Create staging environment

### Long Term (Production Ready)
1. ⏳ Performance testing
2. ⏳ Security scanning
3. ⏳ Backup and disaster recovery
4. ⏳ Monitoring and alerting
5. ⏳ Auto-scaling configuration

---

## 🎯 Deployment Checklist

### Pre-Deployment
- [ ] All 12-factor principles implemented
- [ ] Health checks working
- [ ] Logs streaming to stdout
- [ ] Configuration externalized
- [ ] Database migrations ready
- [ ] Tests passing
- [ ] Docker images built and tested

### GCP Setup
- [ ] GCP project created
- [ ] Billing enabled
- [ ] APIs enabled (GKE, Cloud SQL, Container Registry)
- [ ] Service accounts configured
- [ ] IAM permissions set

### Kubernetes Deployment
- [ ] Deployment manifests created
- [ ] Services configured
- [ ] Ingress/Load balancer set up
- [ ] Secrets created
- [ ] ConfigMaps defined
- [ ] Resource limits set

### Post-Deployment
- [ ] Smoke tests passed
- [ ] Monitoring active
- [ ] Alerts configured
- [ ] Documentation updated
- [ ] Team trained on operations

---

**Status**: Initial setup complete. Ready for feature development with cloud-native principles in mind.

*Last Updated: November 30, 2024*
