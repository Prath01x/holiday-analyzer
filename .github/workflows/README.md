# CI/CD Pipeline Documentation

This directory contains GitHub Actions workflows for automated testing, building, and deployment.

## Workflows

### 1. Backend CI (`backend-ci.yml`)
**Triggers:** Push/PR to main, develop, ci-cd-pipeline branches (backend changes only)

**Jobs:**
- **Test**: Runs backend tests with PostgreSQL service
  - Sets up Java 21 and Maven
  - Runs unit tests
  - Builds the application
  - Uploads test results as artifacts
  
- **Code Quality**: Runs static analysis
  - Checkstyle for code style
  - SpotBugs for bug detection

### 2. Frontend CI (`frontend-ci.yml`)
**Triggers:** Push/PR to main, develop, ci-cd-pipeline, frontend-basics branches (frontend changes only)

**Jobs:**
- **Lint**: ESLint and TypeScript checks
  - Runs ESLint on all TypeScript files
  - Validates TypeScript compilation
  
- **Test**: Runs frontend tests
  - Executes test suite (when configured)
  
- **Build**: Builds production bundle
  - Creates optimized production build
  - Uploads build artifacts

### 3. Docker Build (`docker-build.yml`)
**Triggers:** Push to main/develop, tags starting with 'v', PRs to main

**Jobs:**
- **Build Backend**: Builds and pushes backend Docker image
  - Uses Docker Buildx for multi-platform builds
  - Pushes to GitHub Container Registry (ghcr.io)
  - Tags with branch name, PR number, version, and commit SHA
  
- **Build Frontend**: Builds and pushes frontend Docker image
  - Same process as backend
  
- **Test Docker Compose**: Tests full stack deployment
  - Starts all services with docker-compose
  - Validates health endpoints
  - Only runs on pull requests

### 4. Code Quality (`code-quality.yml`)
**Triggers:** Push/PR to main, develop, ci-cd-pipeline, frontend-basics branches

**Jobs:**
- **Prettier Check**: Validates code formatting
- **Dependency Review**: Checks for vulnerable dependencies (PRs only)
- **Security Scan**: Runs Trivy vulnerability scanner
  - Scans filesystem for vulnerabilities
  - Uploads results to GitHub Security tab

## Setup Instructions

### Prerequisites
1. GitHub repository with Actions enabled
2. Permissions for GitHub Container Registry (automatic)

### Configuration

#### 1. Enable GitHub Actions
- Go to repository Settings → Actions → General
- Enable "Allow all actions and reusable workflows"

#### 2. Container Registry Permissions
- Go to repository Settings → Actions → General
- Under "Workflow permissions", select "Read and write permissions"

#### 3. Branch Protection (Optional but Recommended)
- Go to repository Settings → Branches
- Add branch protection rule for `main`:
  - Require status checks to pass before merging
  - Select: Backend CI, Frontend CI, Code Quality

### Local Development

#### Run Linting
```bash
# Frontend
cd frontend
npm run lint
npm run format:check

# Fix issues automatically
npm run lint:fix
npm run format
```

#### Run Tests
```bash
# Backend
cd backend
mvn test

# Frontend
cd frontend
npm test
```

#### Build Docker Images Locally
```bash
# Backend
docker build -t holiday-analyzer-backend ./backend

# Frontend
docker build -t holiday-analyzer-frontend ./frontend

# Full stack
docker-compose up --build
```

## Viewing Results

### GitHub Actions Tab
- Go to repository → Actions tab
- View all workflow runs
- Click on any run to see detailed logs

### Artifacts
- Test results and build artifacts are uploaded
- Download from workflow run page

### Security Alerts
- Go to repository → Security tab
- View Dependabot alerts and Trivy scan results

## Troubleshooting

### Workflow Fails on Test
- Check test logs in Actions tab
- Ensure PostgreSQL service is healthy
- Verify environment variables

### Docker Build Fails
- Check Dockerfile syntax
- Verify all dependencies are available
- Check Docker Buildx logs

### Linting Errors
- Run `npm run lint:fix` locally
- Run `npm run format` to auto-format
- Fix remaining issues manually

## Future Enhancements

- [ ] Add code coverage reporting (Codecov/Coveralls)
- [ ] Add performance testing
- [ ] Add E2E tests with Playwright
- [ ] Add automatic deployment to staging/production
- [ ] Add Slack/Discord notifications
- [ ] Add release automation
