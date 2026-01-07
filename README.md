# Holiday Analyzer

[![Backend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/backend-ci.yml)
[![Frontend CI](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/frontend-ci.yml)
[![Docker Build](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/docker-build.yml)
[![Code Quality](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml/badge.svg)](https://github.com/Prath01x/holiday-analyzer/actions/workflows/code-quality.yml)

A cloud-native application for analyzing public holidays across European countries.

## 📋 Project Overview

This application is built as part of the Cloud Native Software Engineering course. It collects and analyzes public holiday data from European countries to identify travel patterns and peak vacation periods.

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
├── frontend/            # React + TypeScript application
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

This is the recommended way to run the entire application:

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
   - You'll see a 404 error page - this is normal, no endpoints are defined yet
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

## 👥 Team Members

- Lucas [Matrikelnummer]
- [Name 2] [Matrikelnummer]
- [Name 3] [Matrikelnummer]

---

## 📚 Next Steps

1. ✅ Project setup complete
2. ⏳ Create database entities (Country, Holiday)
3. ⏳ Implement REST API endpoints
4. ⏳ Build frontend components
5. ⏳ Integrate external holiday APIs
6. ⏳ Deploy to Google Cloud Platform (GKE)

---

## 📄 License

This project is created for educational purposes as part of the Cloud Native Software Engineering course.

---

## 🆘 Need Help?

- Check the troubleshooting section above
- Review Docker logs: `docker-compose logs -f`
- Ensure all prerequisites are installed
- Verify no `#` character in folder path (Vite requirement)
