#!/bin/bash
# Start production environment

echo "Stopping any running containers..."
docker-compose down

echo "Starting services in production mode..."
docker-compose --profile prod up --build -d

echo "Waiting for services to be ready..."
sleep 5

echo "Checking service status..."
docker-compose --profile prod ps

echo ""
echo "Services are starting up!"
echo "- Application: http://localhost:3000"
echo "- API: http://localhost:8080"
echo "- Database: localhost:3306"
echo ""
echo "To view logs: docker-compose logs -f"
echo "To stop: docker-compose down"
