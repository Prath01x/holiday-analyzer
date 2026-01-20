# Holiday Analyzer

[![Backend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml)
[![Frontend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml)
[![Docker Build](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml)
[![Code Quality](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml)

A cloud-native application for analyzing public holidays and vacation periods across European countries. Built with Spring Boot, React, and deployed on Google Kubernetes Engine (GKE).

---

## 📋 Table of Contents

- [Team](#-team)
- [Project Overview](#-project-overview)
- [Features](#-features--usage)
- [Technology Stack](#-technology-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
- [Deployment](#-deployment)
- [Configuration](#-configuration)
- [Authentication](#-authentication)
- [Database Migrations](#-database-migrations)
- [Logging](#-logging)
- [12-Factor App Compliance](#-12-factor-app-compliance)
- [Git Workflow](#-git-workflow)
- [Troubleshooting](#-troubleshooting)

---

## 👥 Team

| Name | Student ID |
|------|------------|
| Lucas Schiegl | 877299     |
| Pratham Malhotra | 889343     |

**Course**: Cloud Native Software Engineering  
**Institution**: Hochschule Kaiserslautern

---

## 📋 Project Overview

This application was developed as part of the Cloud Native Software Engineering course. It helps users identify optimal travel periods by analyzing when many or few people in a destination country are on vacation.

### Live Demo

**🌐 Production**: http://35.242.201.112 (when cluster is running)

**Default Admin Credentials**:
- Username: `admin`
- Password: `admin123`

⚠️ **Change these in production!**

---

## 🎯 Features & Usage

### For Users: Vacation Planning

1. **Select a Country**: Choose your travel destination
2. **Analyze Calendar**: Color-coded system shows vacation density:
   - 🟢 **Green**: Few people on vacation – ideal travel time
   - 🟡 **Yellow**: Medium occupancy
   - 🔴 **Red**: Many people on vacation – highly frequented periods
3. **Select Time Period**: View detailed information:
   - Which regions have time off
   - Population affected per region
   - Type of vacation (public holidays vs school vacations)

### For Administrators: Data Management

- **Create Countries**: Add European countries with population data
- **Manage Regions**: Create states, cantons, or administrative units
- **Add Public Holidays**: Record national and regional holidays
- **Maintain Vacation Periods**: Create school holidays per region

---

## 🛠️ Technology Stack

### Backend
- **Java 21** with **Spring Boot 3.2.0**
- **Spring Data JPA** for database operations
- **Spring Security** with JWT authentication
- **PostgreSQL** database
- **Flyway** for database migrations
- **Maven** for dependency management
- **Logstash** for structured JSON logging

### Frontend
- **React 18** with **TypeScript**
- **Vite** for build tooling
- **React Router** for navigation
- **Nginx** for production serving

### DevOps & Cloud
- **Docker** & **Docker Compose** for containerization
- **Kubernetes** (GKE Autopilot) for orchestration
- **Google Cloud SQL** (PostgreSQL 15)
- **Google Container Registry** (GCR) for images
- **GitHub Actions** for CI/CD pipeline
- **Cloud SQL Proxy** for secure database access
- **Workload Identity** for GCP authentication

---

## 🏗️ Architecture

### Project Structure

```
holiday-analyzer/
├── backend/                    # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/holidayanalyzer/
│   │   │   │   ├── controller/      # REST controllers
│   │   │   │   ├── service/         # Business logic
│   │   │   │   ├── repository/      # Data access
│   │   │   │   ├── model/           # JPA entities
│   │   │   │   ├── dto/             # Data transfer objects
│   │   │   │   ├── security/        # JWT & authentication
│   │   │   │   └── config/          # Spring configuration
│   │   │   └── resources/
│   │   │       ├── db/migration/    # Flyway migrations
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties
│   │   │       ├── application-prod.properties
│   │   │       └── logback-spring.xml
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # React + TypeScript application
│   ├── src/
│   │   ├── components/         # React components
│   │   ├── api.ts             # API client
│   │   └── types.ts           # TypeScript types
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── vite.config.ts
├── k8s/                       # Kubernetes manifests
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secrets.yaml
│   ├── backend/
│   │   ├── backend-deployment.yaml
│   │   └── backend-service.yaml
│   ├── frontend/
│   │   ├── frontend-deployment.yaml
│   │   └── frontend-service.yaml
│   └── ingress.yaml
├── .github/workflows/         # CI/CD pipelines
├── docker-compose.yml
├── .env.example
└── README.md
```

### Cloud Architecture

```
┌─────────────────────────────────────────────────┐
│           Google Cloud Platform (GCP)           │
│                                                  │
│  ┌────────────────────────────────────────────┐ │
│  │   GKE Autopilot Cluster                    │ │
│  │                                             │ │
│  │  ┌──────────────┐      ┌──────────────┐   │ │
│  │  │   Frontend   │      │   Backend    │   │ │
│  │  │   (Nginx)    │◄────►│ (Spring Boot)│   │ │
│  │  │  Replicas: 2 │      │  Replicas: 2 │   │ │
│  │  └──────┬───────┘      └──────┬───────┘   │ │
│  │         │                     │            │ │
│  │         │                     │            │ │
│  │  ┌──────▼───────┐      ┌─────▼────────┐   │ │
│  │  │ LoadBalancer │      │ Cloud SQL    │   │ │
│  │  │   Service    │      │    Proxy     │   │ │
│  │  └──────────────┘      └──────┬───────┘   │ │
│  │                               │            │ │
│  └───────────────────────────────┼────────────┘ │
│                                  │               │
│                          ┌───────▼────────┐      │
│                          │   Cloud SQL    │      │
│                          │  (PostgreSQL)  │      │
│                          └────────────────┘      │
└─────────────────────────────────────────────────┘
```

---

## 🚀 Getting Started

### Prerequisites

**For Docker (Recommended):**
- Docker Desktop installed and running

**For Local Development:**
- Java 21 or higher
- Node.js 18 or higher
- PostgreSQL 15
- Maven 3.9+
- IntelliJ IDEA (recommended) or any Java IDE

---

### Option 1: Running with Docker (Easiest)

This is the recommended way to run the entire application locally:

```bash
# Navigate to project directory
cd holiday-analyzer

# Start all services (database, backend, frontend)
docker-compose up --build

# Or run in detached mode (background)
docker-compose up -d --build

# Stop all services
docker-compose down

# Stop and remove volumes (clean database)
docker-compose down -v
```

**Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Database: localhost:5432

**View logs:**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

---

### Option 2: Running Locally (Development)

#### Step 1: Start PostgreSQL Database

**Option A: Using Docker (Recommended)**
```bash
docker-compose up postgres
```

**Option B: Using Local PostgreSQL**
- Ensure PostgreSQL is running
- Create database: `holidays`
- User: `postgres`
- Password: `password`

#### Step 2: Run Backend

**Using IntelliJ IDEA (Recommended):**
1. Open the `backend` folder in IntelliJ IDEA
2. Wait for Maven dependencies to download
3. Open `HolidayAnalyzerApplication.java`
4. Click the green ▶️ play button next to the `main` method
5. Backend will start on http://localhost:8080

**Using Command Line:**
```bash
cd backend

# If you have Maven installed
mvn spring-boot:run

# Or use Maven wrapper
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

#### Step 3: Run Frontend

Open a **new terminal**:

```bash
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

Frontend will start on http://localhost:5173

---

## 🌐 Deployment

### Google Kubernetes Engine (GKE) Deployment

Complete deployment guide for GKE with Cloud SQL.

#### Prerequisites

- Google Cloud SDK installed (`gcloud auth login`)
- kubectl installed
- Project: `august-impact-479818-r1`
- Cloud SQL instance `holiday-analyzer-db` (PostgreSQL)
- Docker images in Google Container Registry (GCR)
- **Owner/Editor role** in GCP project

---

### Initial Setup (First Time Only)

#### 1. Cloud SQL Instance

```bash
# Create Cloud SQL instance (if not exists)
gcloud sql instances create holiday-analyzer-db \
  --database-version=POSTGRES_15 \
  --tier=db-f1-micro \
  --region=europe-west3 \
  --project=august-impact-479818-r1

# Or resume if paused
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=ALWAYS \
  --project=august-impact-479818-r1
```

#### 2. GKE Autopilot Cluster

```bash
gcloud container clusters create-auto holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```

#### 3. Configure kubectl

```bash
gcloud container clusters get-credentials holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1
```

#### 4. Workload Identity Setup

**IMPORTANT**: Only needed **once** during initial setup!

```bash
# 4.1 Create Kubernetes ServiceAccount
kubectl create serviceaccount backend-sa -n holiday-analyzer

# 4.2 Create Google ServiceAccount
gcloud iam service-accounts create backend-cloudsql-sa \
  --display-name="Backend Cloud SQL Service Account" \
  --project=august-impact-479818-r1

# 4.3 Grant Cloud SQL Client role
gcloud projects add-iam-policy-binding august-impact-479818-r1 \
  --member="serviceAccount:backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com" \
  --role="roles/cloudsql.client"

# 4.4 Create Workload Identity binding
gcloud iam service-accounts add-iam-policy-binding \
  backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com \
  --role roles/iam.workloadIdentityUser \
  --member "serviceAccount:august-impact-479818-r1.svc.id.goog[holiday-analyzer/backend-sa]" \
  --project=august-impact-479818-r1

# 4.5 Grant Token Creator role
gcloud iam service-accounts add-iam-policy-binding \
  backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com \
  --role roles/iam.serviceAccountTokenCreator \
  --member "serviceAccount:august-impact-479818-r1.svc.id.goog[holiday-analyzer/backend-sa]" \
  --project=august-impact-479818-r1

# 4.6 Annotate Kubernetes ServiceAccount
kubectl annotate serviceaccount backend-sa -n holiday-analyzer \
  iam.gke.io/gcp-service-account=backend-cloudsql-sa@august-impact-479818-r1.iam.gserviceaccount.com
```

**What is Workload Identity?**
- Enables backend pods to securely access Cloud SQL
- Uses `serviceAccountName: backend-sa` in deployment
- Without this: **403 Forbidden** errors when accessing Cloud SQL

---

### Build and Push Docker Images

```bash
# Backend
docker build -t gcr.io/august-impact-479818-r1/holiday-backend:latest -f backend/Dockerfile backend
docker push gcr.io/august-impact-479818-r1/holiday-backend:latest

# Frontend
docker build -t gcr.io/august-impact-479818-r1/holiday-frontend:latest -f frontend/Dockerfile frontend
docker push gcr.io/august-impact-479818-r1/holiday-frontend:latest
```

---

### Deploy to Kubernetes

```bash
# 1. Create namespace
kubectl apply -f k8s/namespace.yaml

# 2. Apply ConfigMap and Secrets
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml

# 3. Deploy backend (includes Cloud SQL Proxy)
kubectl apply -f k8s/backend/

# 4. Deploy frontend
kubectl apply -f k8s/frontend/

# 5. (Optional) Apply Ingress
kubectl apply -f k8s/ingress.yaml

# 6. Check status
kubectl get pods -n holiday-analyzer
kubectl get svc -n holiday-analyzer

# 7. Get frontend URL
kubectl get svc frontend-service -n holiday-analyzer
```

---

### Update Deployment (After Code Changes)

```bash
# Rebuild and push images
docker build -t gcr.io/august-impact-479818-r1/holiday-backend:latest -f backend/Dockerfile backend
docker push gcr.io/august-impact-479818-r1/holiday-backend:latest

docker build -t gcr.io/august-impact-479818-r1/holiday-frontend:latest -f frontend/Dockerfile frontend
docker push gcr.io/august-impact-479818-r1/holiday-frontend:latest

# Restart deployments to pull new images
kubectl rollout restart deploy/backend -n holiday-analyzer
kubectl rollout restart deploy/frontend -n holiday-analyzer

# Check rollout status
kubectl rollout status deploy/backend -n holiday-analyzer
kubectl rollout status deploy/frontend -n holiday-analyzer
```

---

### Graceful Shutdown (Save Costs)

**Simple 2-step process:**

```bash
# Step 1: Delete cluster (removes all pods, services, load balancer)
gcloud container clusters delete holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1 \
  --quiet

# Step 2: Pause Cloud SQL (costs only ~$0.50/month)
gcloud sql instances patch holiday-analyzer-db \
  --activation-policy=NEVER \
  --project=august-impact-479818-r1
```

**Cost Savings:**
- Before: ~$70-100/month (cluster) + ~$30/month (Cloud SQL) = **~$100-130/month**
- After: $0 (cluster deleted) + $0.50 (Cloud SQL paused) = **~$0.50/month** 💰

**What's preserved (FREE):**
- ✅ Docker images in GCR
- ✅ Service accounts
- ✅ Workload Identity configuration
- ✅ All database data

---

### Restart Deployment

**Simple process - NO Workload Identity setup needed!**

```bash
# Step 1: Resume Cloud SQL
gcloud sql instances patch holiday-analyzer-db `
  --activation-policy=ALWAYS `
  --project=august-impact-479818-r1

# Step 2: Create cluster
gcloud container clusters create-auto holiday-analyzer-cluster '
  --region=europe-west3 '
  --project=august-impact-479818-r1

# Step 3: Configure kubectl
gcloud container clusters get-credentials holiday-analyzer-cluster \
  --region=europe-west3 \
  --project=august-impact-479818-r1

# Step 4: Deploy application
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secrets.yaml
kubectl apply -f k8s/backend/
kubectl apply -f k8s/frontend/

# Step 5: Check status
kubectl get pods -n holiday-analyzer
kubectl get svc frontend-service -n holiday-analyzer
```

**Important:**
- ⚠️ **NO Workload Identity setup needed** - already configured!
- ⚠️ **NO YAML changes needed** - everything stays the same!
- ✅ Takes ~5-10 minutes until everything is running
- ✅ Data is preserved (Flyway migrations handle schema)

---

### Quick Commands

```bash
# Check pod status
kubectl get pods -n holiday-analyzer

# Check services
kubectl get svc -n holiday-analyzer

# Get frontend URL
kubectl get svc frontend-service -n holiday-analyzer -o jsonpath='{.status.loadBalancer.ingress[0].ip}'

# View backend logs
kubectl logs -l app=backend -n holiday-analyzer -c backend --tail=50

# View frontend logs
kubectl logs -l app=frontend -n holiday-analyzer --tail=50

# Describe pod (for troubleshooting)
kubectl describe pod <pod-name> -n holiday-analyzer
```

---

## ⚙️ Configuration

### Environment Variables

The application follows **12-Factor App: Factor III (Config)** - all configuration via environment variables.

#### Backend Configuration

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod) | `dev` | No |
| `SERVER_PORT` | Backend server port | `8080` | No |
| `DB_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/holidays` | Yes |
| `DB_USERNAME` | Database username | `postgres` | Yes |
| `DB_PASSWORD` | Database password | `password` | Yes |
| `JWT_SECRET` | JWT signing secret | (auto-generated) | No |
| `JWT_EXPIRATION` | JWT expiration (ms) | `86400000` (24h) | No |

#### Frontend Configuration

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `VITE_API_BASE_URL` | Backend API endpoint | `http://localhost:8080` | No |

---

### Configuration Profiles

#### Development Profile (`dev`)
- Verbose logging (DEBUG level)
- SQL queries printed to console
- Human-readable logs
- Suitable for local development

#### Production Profile (`prod`)
- Minimal logging (WARN/INFO level)
- No SQL queries in logs
- JSON structured logs
- Security headers enabled
- Error details hidden

---

### Setup `.env` File

```bash
# Copy example file
cp .env.example .env

# Edit with your values
nano .env
```

Example `.env`:
```env
# Application Profile
SPRING_PROFILES_ACTIVE=dev

# Server Configuration
SERVER_PORT=8080

# Database Configuration
DB_URL=jdbc:postgresql://localhost:5432/holidays
DB_USERNAME=postgres
DB_PASSWORD=password

# Frontend Configuration
VITE_API_BASE_URL=http://localhost:8080

# Docker Configuration
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
POSTGRES_DB=holidays
```

---

## 🔐 Authentication

### JWT-Based Authentication System

The application uses JWT (JSON Web Tokens) for stateless authentication.

#### Features
- **JWT Token Authentication** - Secure, stateless
- **BCrypt Password Hashing** - Industry-standard encryption
- **Protected Routes** - Admin panel requires authentication
- **Session Management** - Token stored in localStorage
- **Auto-login** - Persistent sessions across browser refreshes

#### Default Credentials

```
Username: admin
Password: admin123
```

⚠️ **IMPORTANT**: Change default password in production!

#### API Endpoints

**Public Endpoints** (No authentication required):
```
POST /api/auth/login
GET  /api/countries/**
GET  /api/subdivisions/**
GET  /api/holidays/**
GET  /api/school-holidays/**
GET  /api/analysis/**
```

**Protected Endpoints** (Require JWT token):
```
POST   /api/countries/**
PUT    /api/countries/**
DELETE /api/countries/**
POST   /api/holidays/**
PUT    /api/holidays/**
DELETE /api/holidays/**
... (all POST/PUT/DELETE operations)
```

#### Login Flow

1. Navigate to `/login`
2. Enter credentials (default: admin/admin123)
3. On success, JWT token stored in localStorage
4. User redirected to `/admin`
5. All subsequent API requests include the token

#### Making Authenticated Requests

```typescript
const token = localStorage.getItem('token');

fetch('http://localhost:8080/api/countries', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify(countryData)
});
```

---

## 🗄️ Database Migrations

### Flyway Database Migrations

The application uses **Flyway** for database schema management, implementing **12-Factor App: Factor XII (Admin Processes)**.

#### Why Flyway?

- ✅ Version-controlled database schema
- ✅ Automatic migrations on startup
- ✅ Idempotent operations
- ✅ Dev/Prod parity
- ✅ One-off admin processes (12-Factor compliant)

#### Migration Files

Located in: `backend/src/main/resources/db/migration/`

**Current Migrations:**
1. `V1__Initial_schema.sql` - Creates all tables, indexes, constraints
2. `V2__Seed_initial_data.sql` - Seeds admin user

#### How It Works

1. **Automatic Execution**: Migrations run on application startup
2. **Version Tracking**: Flyway tracks applied migrations in `flyway_schema_history` table
3. **Idempotent**: Each migration runs only once
4. **Ordered**: Migrations run in version order (V1, V2, V3, etc.)

#### Creating New Migrations

```bash
# Create new migration file
touch backend/src/main/resources/db/migration/V3__Add_new_feature.sql
```

Example migration:
```sql
-- V3__Add_user_preferences.sql

ALTER TABLE users ADD COLUMN last_login TIMESTAMP;

CREATE TABLE user_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    theme VARCHAR(20) DEFAULT 'light',
    CONSTRAINT fk_preferences_user FOREIGN KEY (user_id) REFERENCES users(id)
);
```

Restart application - Flyway applies the new migration automatically.

#### Configuration

```properties
# application.properties
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true

# Hibernate DDL disabled - Flyway manages schema
spring.jpa.hibernate.ddl-auto=none
```

#### Troubleshooting Migrations

**Migration Failed:**
```bash
# Fix SQL in migration file
# Delete failed entry from flyway_schema_history
DELETE FROM flyway_schema_history WHERE version = 'X';

# Restart application
```

**Reset Database (Development Only):**
```bash
docker-compose down -v
docker-compose up -d postgres
docker-compose up backend
```

---

## 📊 Logging

### Structured Logging System

Implements **12-Factor App: Factor XI (Logs)** - logs as event streams.

#### Features

- **Stdout/Stderr Output** - No log files
- **JSON Structured Logs** - Production format
- **Human-Readable Logs** - Development format
- **Profile-Based Configuration** - Different formats per environment
- **MDC Support** - Correlation IDs for request tracking

#### Log Levels

| Level | Usage | Example |
|-------|-------|---------|
| ERROR | System errors, exceptions | Database connection failed |
| WARN | Warning conditions | Deprecated API usage |
| INFO | Important events | Application started, User logged in |
| DEBUG | Detailed information | Request parameters, Query results |

#### Configuration

**Development (Human-Readable):**
```
2026-01-20 20:00:00.123 [http-nio-8080-exec-1] INFO  c.h.controller.HolidayController - Fetching holidays
2026-01-20 20:00:00.456 [http-nio-8080-exec-1] DEBUG c.h.controller.HolidayController - Found 150 holidays
```

**Production (JSON):**
```json
{
  "@timestamp": "2026-01-20T20:00:00.123Z",
  "level": "INFO",
  "logger_name": "com.holidayanalyzer.controller.HolidayController",
  "message": "Fetching holidays",
  "thread_name": "http-nio-8080-exec-1",
  "correlationId": "abc123-def456-ghi789",
  "application": "holiday-analyzer-backend",
  "environment": "production"
}
```

#### Viewing Logs

**Docker:**
```bash
docker-compose logs -f backend
docker-compose logs --tail=100 backend
```

**Kubernetes:**
```bash
kubectl logs -f deployment/backend -n holiday-analyzer
kubectl logs -f -l app=backend -n holiday-analyzer
kubectl logs --timestamps deployment/backend -n holiday-analyzer
```

**Google Cloud Logging:**
```bash
gcloud logging read "resource.type=k8s_container"
gcloud logging tail
```

---

## ✅ 12-Factor App Compliance

**Status: FULLY COMPLIANT - 12/12 Factors** 🎉

| Factor | Status | Implementation |
|--------|--------|----------------|
| I. Codebase | ✅ | Git repository with multiple deployments |
| II. Dependencies | ✅ | Maven + npm with explicit declarations |
| III. Config | ✅ | Environment variables + ConfigMaps/Secrets |
| IV. Backing Services | ✅ | PostgreSQL as attached resource |
| V. Build, Release, Run | ✅ | Multi-stage Docker builds + K8s |
| VI. Processes | ✅ | Stateless Spring Boot application |
| VII. Port Binding | ✅ | Self-contained services on configurable ports |
| VIII. Concurrency | ✅ | Horizontal scaling with K8s replicas |
| IX. Disposability | ✅ | Fast startup + graceful shutdown |
| X. Dev/Prod Parity | ✅ | Same DB, Flyway migrations, ddl-auto=none |
| XI. Logs | ✅ | JSON structured logging with Logstash |
| XII. Admin Processes | ✅ | Flyway database migrations |

**Overall Score: 12/12 (100%)**

For detailed compliance report, see `12_FACTOR_COMPLIANCE.md`.

---

## 🔀 Git Workflow

### Branch Structure

- **`main`** - Production-ready code (protected)
- **`develop`** - Integration branch for features
- **`feature/*`** - New features (branch from develop)
- **`bugfix/*`** - Bug fixes (branch from develop)
- **`hotfix/*`** - Critical production fixes (branch from main)

### Workflow

```bash
# Start new feature
git checkout develop
git pull origin develop
git checkout -b feature/your-feature-name

# Make changes and commit
git add .
git commit -m "feat: add new feature"
git push origin feature/your-feature-name

# Create Pull Request on GitHub
# After review and approval, merge to develop
```

### Commit Message Convention

Use conventional commits:

```
<type>(<scope>): <subject>

Examples:
feat(backend): add Country entity and repository
fix(frontend): resolve API connection timeout
docs: update README with deployment instructions
chore(deps): update Spring Boot to 3.2.1
```

**Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`, `perf`

---

## 🐛 Troubleshooting

### Backend Issues

**Error: "Cannot connect to database"**
- Check `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` are set correctly
- Ensure PostgreSQL is running: `docker ps`
- Verify database exists: `holidays`

**Error: "Port 8080 already in use"**
```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

**Error: "Flyway migration failed"**
- Check migration SQL syntax
- Delete failed entry from `flyway_schema_history`
- Restart application

### Frontend Issues

**Error: "Failed to load /src/main.tsx"**
- Clear node_modules: `rm -rf node_modules && npm install`
- Ensure no `#` character in folder path
- Restart dev server

**Error: "Port 5173 already in use"**
- Stop other Vite instances
- Or change port in `vite.config.ts`

**Error: "API connection timeout"**
- Verify backend is running
- Check `VITE_API_BASE_URL` is correct
- Check CORS configuration

### Docker Issues

**Error: "Port already allocated"**
```bash
docker-compose down
docker ps  # Check what's using the port
```

**Error: "Build failed"**
```bash
docker-compose down
docker-compose up --build --force-recreate
```

### Kubernetes Issues

**Pods in CrashLoopBackOff:**
```bash
# Check logs
kubectl logs <pod-name> -n holiday-analyzer

# Describe pod for events
kubectl describe pod <pod-name> -n holiday-analyzer
```

**403 Forbidden from Cloud SQL:**
- Verify Workload Identity is configured
- Check service account annotations
- Verify IAM roles are granted

**LoadBalancer stuck in Pending:**
- Wait 2-3 minutes for GCP to provision
- Check GCP quotas
- Verify cluster has external IP allocation

---

## 📚 Additional Resources

- [12-Factor App Methodology](https://12factor.net/)
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [React Documentation](https://react.dev/)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Google Cloud Documentation](https://cloud.google.com/docs)
- [Flyway Documentation](https://flywaydb.org/documentation/)

---

## 📝 License

Part of the Holiday Analyzer project - Cloud Native Software Engineering course.

---

## 🎯 Quick Start Checklist

### Local Development
- [ ] Install Docker Desktop
- [ ] Clone repository
- [ ] Run `docker-compose up --build`
- [ ] Access http://localhost:3000
- [ ] Login with admin/admin123

### GKE Deployment
- [ ] Install gcloud CLI and kubectl
- [ ] Create/resume Cloud SQL instance
- [ ] Create GKE cluster
- [ ] Configure Workload Identity (first time only)
- [ ] Build and push Docker images
- [ ] Apply Kubernetes manifests
- [ ] Access via LoadBalancer IP

### After Code Changes
- [ ] Commit changes to Git
- [ ] Rebuild Docker images
- [ ] Push to GCR
- [ ] Restart Kubernetes deployments
- [ ] Verify deployment status

---

**Last Updated**: January 20, 2026  
**Version**: 1.0.0  
**Status**: ✅ Production Ready - Fully 12-Factor Compliant
