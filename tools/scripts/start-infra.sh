#!/bin/bash

# Start infrastructure only (MySQL + RabbitMQ)
# Use this for backend development when running apps in IDE

set -e

echo "🚀 Starting LifePuzzle Infrastructure (MySQL + RabbitMQ)"
echo "===================================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

cd "$DOCKER_DIR"

# Start infrastructure services
echo -e "${BLUE}🔨 Starting infrastructure services...${NC}"
docker-compose up -d

# Wait for services
echo -e "${BLUE}⏳ Waiting for services to be ready...${NC}"
sleep 10

# Show status
echo ""
echo -e "${GREEN}✅ Infrastructure is running!${NC}"
echo ""
echo -e "${BLUE}📊 Service Status:${NC}"
docker-compose ps

echo ""
echo -e "${BLUE}🌐 Available Services:${NC}"
echo -e "  • ${GREEN}MySQL Database${NC}:     localhost:3306"
echo -e "  • ${GREEN}RabbitMQ${NC}:           localhost:5672"
echo -e "  • ${GREEN}RabbitMQ Management${NC}: http://localhost:15672"

echo ""
echo -e "${BLUE}💡 Next Steps:${NC}"
echo "  • Run your applications in IDE for debugging"
echo "  • Use connection details above in your app configuration"
echo "  • Stop services: ./tools/scripts/stop.sh"