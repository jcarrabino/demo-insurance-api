# Docker Compose Profiles Guide

## Overview

This project uses Docker Compose **profiles** to manage different environments and prevent container conflicts.

## Why Profiles?

The production UI (`insurance-ui`) and development UI (`insurance-ui-dev`) cannot run simultaneously because:
- They serve different purposes (nginx vs Vite dev server)
- They would conflict if both tried to serve the frontend
- Dev mode needs HMR, prod mode needs optimized builds

**Solution**: Use profiles to run only one UI container at a time.

## Available Profiles

### 1. Production Profile (`prod`)

**Purpose**: Run the application as it would in production

**Containers**:
- `mysql` (insurance-db) - Database
- `app` (insurance-app) - Spring Boot API
- `ui` (insurance-ui) - nginx serving built React app

**Start Command**:
```bash
# Windows
start-prod.bat

# Linux/Mac
./start-prod.sh

# Manual
docker-compose --profile prod up --build
```

**Access**:
- Frontend: http://localhost:3000
- API: http://localhost:8080
- Database: localhost:3306

**Use When**:
- Testing production builds
- Verifying nginx configuration
- Performance testing
- Final QA before deployment

### 2. Development Profile (`dev`)

**Purpose**: Active development with hot module replacement

**Containers**:
- `mysql` (insurance-db) - Database
- `app` (insurance-app) - Spring Boot API
- `ui-dev` (insurance-ui-dev) - Vite dev server with HMR

**Start Command**:
```bash
# Windows
start-dev.bat

# Linux/Mac
./start-dev.sh

# Manual
docker-compose --profile dev up --build
```

**Access**:
- Frontend: http://localhost:5173
- API: http://localhost:8080
- Database: localhost:3306

**Use When**:
- Developing frontend features
- Testing UI changes
- Debugging React components
- Daily development work

### 3. No Profile (Default)

**Purpose**: Run only backend services

**Containers**:
- `mysql` (insurance-db) - Database
- `app` (insurance-app) - Spring Boot API

**Start Command**:
```bash
docker-compose up --build
```

**Access**:
- API: http://localhost:8080
- Database: localhost:3306

**Use When**:
- Testing API endpoints
- Running frontend locally (outside Docker)
- Backend-only development

## Profile Comparison

| Feature | No Profile | `--profile prod` | `--profile dev` |
|---------|-----------|------------------|-----------------|
| **MySQL** | ✅ | ✅ | ✅ |
| **Spring Boot API** | ✅ | ✅ | ✅ |
| **UI (nginx)** | ❌ | ✅ Port 3000 | ❌ |
| **UI-Dev (Vite)** | ❌ | ❌ | ✅ Port 5173 |
| **HMR** | ❌ | ❌ | ✅ |
| **Production Build** | ❌ | ✅ | ❌ |
| **Best For** | API testing | Production testing | Active development |

## Common Commands

### Start Services

```bash
# Development mode (recommended for coding)
docker-compose --profile dev up --build

# Production mode (test production build)
docker-compose --profile prod up --build

# Backend only (no UI)
docker-compose up --build
```

### Stop Services

```bash
# Stop all containers (any profile)
docker-compose down

# Stop and remove volumes (fresh database)
docker-compose down -v
```

### View Running Containers

```bash
# All containers
docker-compose ps

# Specific profile
docker-compose --profile dev ps
docker-compose --profile prod ps
```

### View Logs

```bash
# All containers
docker-compose logs -f

# Specific service
docker-compose logs -f ui-dev      # Dev UI
docker-compose logs -f ui          # Prod UI
docker-compose logs -f app         # API
docker-compose logs -f mysql       # Database
```

## Switching Between Profiles

**Always stop containers before switching profiles:**

```bash
# Stop current profile
docker-compose down

# Start new profile
docker-compose --profile dev up --build
# or
docker-compose --profile prod up --build
```

## Troubleshooting

### Both UI Containers Running?

**Problem**: You see both `insurance-ui` and `insurance-ui-dev` running.

**Solution**:
```bash
docker-compose down
docker-compose --profile dev up --build  # Choose one
```

### Port Already in Use?

**Problem**: Error: "port is already allocated"

**Solution**:
```bash
# Check what's using the port
netstat -ano | findstr :3000   # Windows
lsof -i :3000                  # Linux/Mac

# Stop all containers
docker-compose down

# Start with correct profile
docker-compose --profile dev up --build
```

### Wrong UI Container Running?

**Problem**: Accessing http://localhost:3000 but want dev mode.

**Solution**:
```bash
docker-compose down
docker-compose --profile dev up --build
# Now access http://localhost:5173
```

### Container Not Starting?

**Check which profile is active:**
```bash
docker-compose ps
```

**Verify profile in command:**
```bash
# Wrong - no UI will start
docker-compose up

# Correct - dev UI starts
docker-compose --profile dev up
```

## Best Practices

### 1. Use Helper Scripts

Instead of remembering profile commands, use the provided scripts:
- `start-dev.bat` / `start-dev.sh` - Development mode
- `start-prod.bat` / `start-prod.sh` - Production mode

### 2. Always Stop Before Switching

```bash
docker-compose down  # Stop current
docker-compose --profile dev up --build  # Start new
```

### 3. Check Running Containers

Before starting, verify what's running:
```bash
docker-compose ps
```

### 4. Use Correct Port

- Dev mode: http://localhost:5173
- Prod mode: http://localhost:3000
- API (both): http://localhost:8080

### 5. Monitor Logs

Keep logs open during development:
```bash
docker-compose logs -f ui-dev
```

## Profile Configuration

Profiles are defined in `docker-compose.yml`:

```yaml
services:
  ui:
    # ... config ...
    profiles:
      - prod    # Only runs with --profile prod

  ui-dev:
    # ... config ...
    profiles:
      - dev     # Only runs with --profile dev
```

## Summary

- **Development**: Use `--profile dev` for HMR and fast iteration
- **Production Testing**: Use `--profile prod` for production-like environment
- **Backend Only**: Use no profile to run just API and database
- **Never run both UI containers simultaneously**
- **Always use `docker-compose down` before switching profiles**

Happy coding! 🚀
