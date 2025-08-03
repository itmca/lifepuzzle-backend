#!/bin/bash

# LifePuzzle Backend Setup Script for Frontend Development
# This script sets up the complete backend environment for frontend developers

set -e

echo "🚀 LifePuzzle Backend Setup for Frontend Development"
echo "=================================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Docker is not running. Please start Docker Desktop and try again.${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Docker is running${NC}"

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

if [ ! -d "$DOCKER_DIR" ]; then
    echo -e "${RED}❌ Docker directory not found: $DOCKER_DIR${NC}"
    exit 1
fi

cd "$DOCKER_DIR"

# Check if .env file exists, if not copy from example
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}⚠️  .env file not found. Creating from .env.example...${NC}"
    if [ -f ".env.example" ]; then
        cp .env.example .env
        echo -e "${BLUE}📝 .env file created. Please update AWS credentials if needed:${NC}"
        echo "   - AWS_ACCESS_KEY_ID"
        echo "   - AWS_SECRET_ACCESS_KEY"
        echo "   - S3_BUCKET"
        echo ""
        echo -e "${YELLOW}💡 You can edit the .env file now or later: nano $DOCKER_DIR/.env${NC}"
        echo ""
        read -p "Press Enter to continue or Ctrl+C to edit .env file first..."
    else
        echo -e "${RED}❌ .env.example file not found${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✅ .env file exists${NC}"
fi

# Check if there are running containers and ask to stop them
if docker-compose ps | grep -q "Up"; then
    echo -e "${YELLOW}⚠️  Some containers are already running.${NC}"
    echo "Running containers:"
    docker-compose ps
    echo ""
    read -p "Do you want to stop them and restart with full stack? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${BLUE}🛑 Stopping existing containers...${NC}"
        docker-compose down
        docker-compose -f docker-compose.full.yml down
    fi
fi

# Pull latest images
echo -e "${BLUE}📦 Pulling latest Docker images...${NC}"
docker-compose -f docker-compose.full.yml pull mysql rabbitmq

# Build and start services
echo -e "${BLUE}🔨 Building and starting all services...${NC}"
echo "This may take a few minutes on first run..."

docker-compose -f docker-compose.full.yml up -d --build

# Wait for services to be healthy
echo -e "${BLUE}⏳ Waiting for services to be healthy...${NC}"

# Function to check service health
check_health() {
    local service=$1
    local url=$2
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ $service is healthy${NC}"
            return 0
        fi
        echo -e "${YELLOW}⏳ Waiting for $service... (attempt $attempt/$max_attempts)${NC}"
        sleep 5
        ((attempt++))
    done
    
    echo -e "${RED}❌ $service failed to become healthy${NC}"
    return 1
}

# Wait for database
echo -e "${BLUE}🔍 Checking database connection...${NC}"
sleep 10  # Give MySQL time to initialize

# Check services
echo -e "${BLUE}🔍 Checking service health...${NC}"
check_health "LifePuzzle API" "http://localhost:8080/actuator/health"
check_health "Image Resizer" "http://localhost:8081/health"

# Show service status
echo ""
echo -e "${GREEN}🎉 All services are running!${NC}"
echo ""
echo -e "${BLUE}📊 Service Status:${NC}"
docker-compose -f docker-compose.full.yml ps

echo ""
echo -e "${BLUE}🌐 Available Services:${NC}"
echo -e "  • ${GREEN}LifePuzzle API${NC}:     http://localhost:8080"
echo -e "    - Health Check:       http://localhost:8080/actuator/health"
echo -e "    - API Documentation:  http://localhost:8080/swagger-ui.html (if enabled)"
echo ""
echo -e "  • ${GREEN}Image Resizer${NC}:      http://localhost:8081"
echo -e "    - Health Check:       http://localhost:8081/health"
echo ""
echo -e "  • ${GREEN}MySQL Database${NC}:     localhost:3306"
echo -e "    - Database: lifepuzzle"
echo -e "    - Username: lifepuzzle"
echo -e "    - Password: lifepuzzlepass"
echo ""
echo -e "  • ${GREEN}RabbitMQ${NC}:           localhost:5672"
echo -e "    - Management UI:      http://localhost:15672"
echo -e "    - Username: lifepuzzle"
echo -e "    - Password: lifepuzzlepass"

echo ""
echo -e "${BLUE}💡 Useful Commands:${NC}"
echo "  • View logs:           ./tools/scripts/logs.sh"
echo "  • Stop all services:   ./tools/scripts/stop.sh"
echo "  • Check health:        ./tools/scripts/health.sh"
echo "  • Clean up:            ./tools/scripts/cleanup.sh"

echo ""
echo -e "${GREEN}✨ Setup complete! Your backend is ready for frontend development.${NC}"