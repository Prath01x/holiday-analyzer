# Holiday Analyzer

[![Backend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml)
[![Frontend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml)
[![Docker Build](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml)
[![Code Quality](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml)

A cloud-native application for analyzing public holidays and vacation periods across European countries.

## 👥 Team

| Name | Student ID |
|------|------------|
| Lucas Schiegl | 877299 |
| Pratham Malhotra | xxx |

## 📋 Project Overview

This application was developed as part of the Cloud Native Software Engineering course at Hochschule Kaiserslautern. It helps users identify optimal travel periods by analyzing when many or few people in a destination country are on vacation.

### Live Demo

The application is currently deployed on Google Cloud Platform and accessible at:

**🌐 http://35.242.201.112**

## 🎯 Features & Usage

### For Users: Vacation Planning

1. **Select a Country**: Choose the country you want to travel to.

2. **Analyze the Calendar View**: The calendar uses a color-coded system to show how busy certain periods are:
   - **Green days**: Few people are on vacation – ideal travel time
   - **Yellow days**: Medium occupancy
   - **Red days**: Many people have holidays or school vacations – highly frequented periods you might want to avoid

3. **Select a Time Period**: Once you select a date range in the calendar, detailed information is displayed:
   - Which regions have time off during this period
   - How many inhabitants in each region are affected
   - Whether it's public holidays or school vacations

This allows you to identify periods when tourist hotspots are less crowded.

### For Administrators: Data Management

As an administrator, you have additional capabilities for data maintenance:

- **Create Countries**: Add new European countries with population data
- **Manage Regions**: Create states, cantons, or other administrative units and assign them to countries
- **Add Public Holidays**: Record national and regional holidays with dates
- **Maintain Vacation Periods**: Create school holidays and other vacation periods per region

## 🛠️ Technology Stack

### Backend
- **Java 21** with **Spring Boot 3.2.0**
- **Spring Data JPA** for database operations
- **PostgreSQL** database
- **Maven** for dependency management

### Frontend
- **React 18** with **TypeScript**
- **Vite** for build tooling
- **Nginx** for production serving

### DevOps
- **Docker** & **Docker Compose** for containerization
- **Kubernetes** for orchestration on Google Cloud Platform
- **PostgreSQL 15** database
- **GitHub Actions** for CI/CD pipeline
- **ESLint** & **Prettier** for code quality

## 📁 Project Structure

```
holiday-analyzer/
├── backend/              # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/             # React + TypeScript application
│   ├── src/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── docker-compose.yml
└── README.md
```

## 🚀 Getting Started

### Prerequisites

**For Docker (Recommended):**
- Docker Desktop installed and running

**For Local Development:**
- Java 21 or higher
- Node.js 18 or higher
- PostgreSQL 15
- IntelliJ IDEA (recommended) or any Java IDE

---

## 🐳 Running with Docker (Easiest)

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

## 💻 Running Locally (Development)

### Step 1: Start PostgreSQL Database

**Option A: Using Docker (Recommended)**
```bash
docker-compose up postgres
```

**Option B: Using Local PostgreSQL**
- Ensure PostgreSQL is running
- Create database: `holidays`
- User: `postgres`
- Password: `password`

### Step 2: Run Backend

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

# Or use Maven wrapper (if available)
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

### Step 3: Run Frontend

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

## 🧪 Testing the Application

Once everything is running:

1. **Frontend**: Open http://localhost:5173 (local) or http://localhost:3000 (Docker)
2. **Backend**: Open http://localhost:8080
   - You'll see a 404 error page – this is normal, the root URL has no endpoint
3. **Database**: Connect to `localhost:5432` with credentials:
   - Database: `holidays`
   - User: `postgres`
   - Password: `password`

---

## 🔧 Development Workflow

### Making Changes

**Backend:**
1. Make changes in `backend/src/main/java/`
2. IntelliJ will auto-reload (or restart the application)
3. Test at http://localhost:8080

**Frontend:**
1. Make changes in `frontend/src/`
2. Vite will hot-reload automatically
3. See changes instantly at http://localhost:5173

### Building for Production

**Backend:**
```bash
cd backend
mvn clean package
# JAR file will be in target/ directory
```

**Frontend:**
```bash
cd frontend
npm run build
# Build files will be in dist/ directory
```

---

## 📝 Configuration

### Backend Configuration

Edit `backend/src/main/resources/application.properties`:

```properties
# Server
server.port=8080

# Database (automatically overridden by Docker environment variables)
spring.datasource.url=jdbc:postgresql://localhost:5432/holidays
spring.datasource.username=postgres
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Frontend Configuration

Edit `frontend/vite.config.ts` for proxy settings:

```typescript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    }
  }
}
```

---

## 🔄 CI/CD Pipeline

This project includes a comprehensive CI/CD pipeline using GitHub Actions:

### Automated Workflows

- **Backend CI**: Runs tests, builds, and code quality checks on backend changes
- **Frontend CI**: Lints, tests, and builds frontend on code changes
- **Docker Build**: Builds and pushes Docker images to GitHub Container Registry
- **Code Quality**: Runs Prettier, dependency review, and security scans

### Running Locally

```bash
# Frontend linting and formatting
cd frontend
npm run lint          # Check for linting errors
npm run lint:fix      # Auto-fix linting errors
npm run format:check  # Check code formatting
npm run format        # Auto-format code

# Backend tests
cd backend
mvn test
```

For more details, see [CI/CD Documentation](.github/workflows/README.md)

---

## 🐛 Troubleshooting

### Backend won't start

**Error: "Cannot connect to database"**
- Ensure PostgreSQL is running: `docker ps` or check PostgreSQL service
- Verify database exists: `holidays`
- Check credentials in `application.properties`

**Error: "Port 8080 already in use"**
```bash
# Windows
netstat -ano | findstr :8080

# Linux/Mac
lsof -i :8080
```

### Frontend won't start

**Error: "Failed to load /src/main.tsx"**
- Clear node_modules: `rm -rf node_modules && npm install`
- Ensure no `#` character in folder path
- Restart dev server

**Error: "Port 5173 already in use"**
- Stop other Vite instances
- Or change port in `vite.config.ts`

### Docker issues

**Error: "Port already allocated"**
```bash
# Stop all containers
docker-compose down

# Check what's using the port
docker ps
```

**Error: "Build failed"**
```bash
# Clean rebuild
docker-compose down
docker-compose up --build --force-recreate
```
