#!/bin/bash

# Start full application stack (Infrastructure + Applications)
# Use this for frontend development

set -e

echo "🚀 Starting LifePuzzle Full Stack"
echo "================================"

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

cd "$DOCKER_DIR"

# Check .env file
if [ ! -f ".env" ]; then
    echo -e "${YELLOW}⚠️  .env file not found. Run ./tools/scripts/setup-dev.sh first${NC}"
    exit 1
fi

# Start full stack
echo -e "${BLUE}🔨 Starting full application stack...${NC}"
docker-compose -f docker-compose.full.yml up -d --build

# Wait for services
echo -e "${BLUE}⏳ Waiting for services to be ready...${NC}"
sleep 15

# Show status
echo ""
echo -e "${GREEN}✅ Full stack is running!${NC}"
echo ""
echo -e "${BLUE}📊 Service Status:${NC}"
docker-compose -f docker-compose.full.yml ps

echo ""
echo -e "${BLUE}🌐 Available Services:${NC}"
echo -e "  • ${GREEN}LifePuzzle API${NC}:     http://localhost:8080"
echo -e "  • ${GREEN}Image Resizer${NC}:      http://localhost:8081"
echo -e "  • ${GREEN}MySQL Database${NC}:     localhost:3306"
echo -e "  • ${GREEN}RabbitMQ Management${NC}: http://localhost:15672"

echo ""
echo -e "${BLUE}💡 Useful Commands:${NC}"
echo "  • View logs: ./tools/scripts/logs.sh"
echo "  • Stop services: ./tools/scripts/stop.sh"