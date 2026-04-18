@echo off
REM Start development environment with hot reload

echo Stopping any running containers...
docker stop insurance-ui-dev
docker-compose down --volumes --remove-orphans

echo Starting services with dev profile...
docker-compose --profile dev up --build -d --force-recreate

echo Waiting for services to be ready...
timeout /t 5 /nobreak > nul

echo Checking service status...
docker-compose --profile dev ps

echo.
echo Services are starting up!
echo - API: http://localhost:8080
echo - UI (Dev with HMR): http://localhost:5173
echo - Database: localhost:3306
echo.
echo Hot Module Replacement is enabled!
echo Edit files in demo-ui/src/** and watch them update instantly.
echo.
echo To view logs: docker-compose logs -f ui-dev
echo To stop: docker-compose down
