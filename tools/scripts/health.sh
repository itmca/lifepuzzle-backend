#!/bin/bash

# Check health status of all LifePuzzle services

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🏥 LifePuzzle Services Health Check${NC}"
echo "=================================="

# Function to check HTTP endpoint
check_http() {
    local name=$1
    local url=$2
    local timeout=${3:-5}
    
    printf "%-20s " "$name:"
    
    if curl -s -f --max-time $timeout "$url" > /dev/null 2>&1; then
        echo -e "${GREEN}✅ Healthy${NC}"
        return 0
    else
        echo -e "${RED}❌ Unhealthy${NC}"
        return 1
    fi
}

# Function to check Docker container
check_container() {
    local name=$1
    local container=$2
    
    printf "%-20s " "$name:"
    
    if docker ps --filter "name=$container" --filter "status=running" --format "{{.Names}}" | grep -q "$container"; then
        # Check if container is healthy (if health check is configured)
        health_status=$(docker inspect --format='{{.State.Health.Status}}' "$container" 2>/dev/null || echo "none")
        case $health_status in
            "healthy")
                echo -e "${GREEN}✅ Healthy${NC}"
                return 0
                ;;
            "unhealthy")
                echo -e "${RED}❌ Unhealthy${NC}"
                return 1
                ;;
            "starting")
                echo -e "${YELLOW}⏳ Starting${NC}"
                return 1
                ;;
            "none")
                echo -e "${GREEN}✅ Running (no health check)${NC}"
                return 0
                ;;
            *)
                echo -e "${YELLOW}⚠️  Unknown status${NC}"
                return 1
                ;;
        esac
    else
        echo -e "${RED}❌ Not running${NC}"
        return 1
    fi
}

# Check infrastructure services
echo -e "${BLUE}🏗️  Infrastructure Services:${NC}"
check_container "MySQL" "lifepuzzle-mysql"
check_container "RabbitMQ" "lifepuzzle-rabbitmq"

echo ""

# Check application services (if running)
echo -e "${BLUE}📱 Application Services:${NC}"
if docker ps --filter "name=lifepuzzle-api" --format "{{.Names}}" | grep -q "lifepuzzle-api"; then
    check_container "LifePuzzle API" "lifepuzzle-api"
    check_http "API Health" "http://localhost:8080/hc"
else
    printf "%-20s " "LifePuzzle API:"
    echo -e "${YELLOW}⚠️  Not running${NC}"
fi

if docker ps --filter "name=lifepuzzle-image-resizer" --format "{{.Names}}" | grep -q "lifepuzzle-image-resizer"; then
    check_container "Image Resizer" "lifepuzzle-image-resizer"
    check_http "Resizer Health" "http://localhost:9000/health"
else
    printf "%-20s " "Image Resizer:"
    echo -e "${YELLOW}⚠️  Not running${NC}"
fi

echo ""

# Check external dependencies
echo -e "${BLUE}🌐 External Dependencies:${NC}"
check_http "RabbitMQ Management" "http://localhost:15672"

echo ""

# Show Docker container status
echo -e "${BLUE}🐳 Docker Container Status:${NC}"
if docker ps --filter "name=lifepuzzle" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -q "lifepuzzle"; then
    docker ps --filter "name=lifepuzzle" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
else
    echo "No LifePuzzle containers running"
fi

echo ""
echo -e "${BLUE}💡 Troubleshooting:${NC}"
echo "  • View logs: ./tools/scripts/logs.sh [service]"
echo "  • Restart services: ./tools/scripts/stop.sh && ./tools/scripts/start-full.sh"
echo "  • Setup from scratch: ./tools/scripts/setup-dev.sh"