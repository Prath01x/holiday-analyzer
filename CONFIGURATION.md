# Configuration Management Guide

This document explains how to configure the Holiday Analyzer application for different environments.

## 12-Factor App Compliance

This application follows **Factor III: Config** of the [12-Factor App](https://12factor.net/config) methodology:
- All configuration is stored in **environment variables**
- No secrets are hardcoded in the codebase
- Different environments (dev, prod) use the same codebase with different config

---

## Environment Variables

### Backend Configuration

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | `dev` | No |
| `SERVER_PORT` | Backend server port | `8080` | No |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/holidays` | Yes |
| `DB_USERNAME` | Database username | `postgres` | Yes |
| `DB_PASSWORD` | Database password | `password` | Yes |

### Frontend Configuration

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `VITE_API_BASE_URL` | Backend API endpoint | `http://localhost:8080` | No |

### Docker Configuration

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `POSTGRES_USER` | PostgreSQL user | `postgres` | No |
| `POSTGRES_PASSWORD` | PostgreSQL password | `password` | No |
| `POSTGRES_DB` | PostgreSQL database name | `holidays` | No |

---

## Configuration Profiles

### Development Profile (`dev`)

**Characteristics:**
- Verbose logging (DEBUG level)
- SQL queries printed to console
- Database schema auto-created
- Suitable for local development

**Activation:**
```bash
# Environment variable
export SPRING_PROFILES_ACTIVE=dev

# Or in .env file
SPRING_PROFILES_ACTIVE=dev
```

**Configuration file:** `application-dev.properties`

---

### Production Profile (`prod`)

**Characteristics:**
- Minimal logging (WARN/INFO level)
- No SQL queries in logs
- Database schema validation only (no auto-creation)
- Security headers enabled
- Error details hidden

**Activation:**
```bash
# Environment variable
export SPRING_PROFILES_ACTIVE=prod

# Or in .env file
SPRING_PROFILES_ACTIVE=prod
```

**Configuration file:** `application-prod.properties`

---

## Setup Instructions

### 1. Local Development (Without Docker)

**Step 1:** Copy the example environment file
```bash
cp .env.example .env
```

**Step 2:** Edit `.env` with your local settings
```env
SPRING_PROFILES_ACTIVE=dev
DB_URL=jdbc:postgresql://localhost:5432/holidays
DB_USERNAME=postgres
DB_PASSWORD=your-password
```

**Step 3:** Run the backend
```bash
cd backend
mvn spring-boot:run
```

The application will automatically load environment variables from your system.

---

### 2. Docker Development

**Step 1:** (Optional) Create `.env` file for custom settings
```bash
cp .env.example .env
```

**Step 2:** Start services
```bash
docker-compose up --build
```

Docker Compose will use:
1. Variables from `.env` file (if exists)
2. Default values specified in `docker-compose.yml`

---

### 3. Production Deployment

**For Cloud Platforms (AWS, GCP, Azure):**

**Step 1:** Set environment variables in your cloud platform

**AWS ECS/Fargate:**
```json
{
  "environment": [
    {"name": "SPRING_PROFILES_ACTIVE", "value": "prod"},
    {"name": "DB_URL", "value": "jdbc:postgresql://your-rds-endpoint:5432/holidays"},
    {"name": "DB_USERNAME", "value": "admin"},
    {"name": "DB_PASSWORD", "valueFrom": "arn:aws:secretsmanager:..."}
  ]
}
```

**Google Cloud Run:**
```bash
gcloud run deploy holiday-analyzer \
  --set-env-vars SPRING_PROFILES_ACTIVE=prod \
  --set-env-vars DB_URL=jdbc:postgresql://... \
  --set-env-vars DB_USERNAME=admin \
  --set-env-vars DB_PASSWORD=...
```

**Kubernetes:**
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  SPRING_PROFILES_ACTIVE: "prod"
  DB_URL: "jdbc:postgresql://postgres-service:5432/holidays"
---
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
type: Opaque
stringData:
  DB_USERNAME: "admin"
  DB_PASSWORD: "your-secure-password"
```

---

## Security Best Practices

### ✅ DO:
- Use environment variables for all configuration
- Store secrets in secret management services (AWS Secrets Manager, GCP Secret Manager)
- Use different credentials for dev/prod
- Rotate passwords regularly
- Use strong passwords in production

### ❌ DON'T:
- Commit `.env` file to git (it's in `.gitignore`)
- Hardcode passwords in code
- Use default passwords in production
- Share production credentials in chat/email
- Commit `application-prod.properties` with real credentials

---

## Verifying Configuration

### Check Active Profile
```bash
# In logs, you should see:
# The following profiles are active: dev
# or
# The following profiles are active: prod
```

### Test Database Connection
```bash
# Backend should start without errors
# Check logs for:
# HikariPool-1 - Start completed.
```

### Test Environment Variables
```bash
# Linux/Mac
echo $SPRING_PROFILES_ACTIVE

# Windows PowerShell
$env:SPRING_PROFILES_ACTIVE

# Docker
docker exec backend env | grep SPRING_PROFILES_ACTIVE
```

---

## Troubleshooting

### Problem: "Could not connect to database"
**Solution:** Check `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` are set correctly

### Problem: "Profile 'prod' not found"
**Solution:** Ensure `application-prod.properties` exists in `src/main/resources/`

### Problem: Environment variables not loading
**Solution:** 
- Check `.env` file exists (for Docker)
- Verify variables are exported (for local)
- Restart application after changing variables

### Problem: Using wrong profile
**Solution:** Set `SPRING_PROFILES_ACTIVE` explicitly:
```bash
export SPRING_PROFILES_ACTIVE=dev
```

---

## Additional Resources

- [12-Factor App: Config](https://12factor.net/config)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [Docker Environment Variables](https://docs.docker.com/compose/environment-variables/)
