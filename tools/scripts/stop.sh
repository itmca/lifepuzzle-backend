#!/bin/bash

# Stop all LifePuzzle services

set -e

echo "🛑 Stopping LifePuzzle Services"
echo "==============================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m'

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

cd "$DOCKER_DIR"

echo -e "${BLUE}🛑 Stopping all services...${NC}"

# Stop both configurations
docker-compose down 2>/dev/null || true
docker-compose -f docker-compose.full.yml down 2>/dev/null || true

echo -e "${GREEN}✅ All services stopped${NC}"

# Show remaining containers (if any)
if docker ps --filter "name=lifepuzzle" --format "table {{.Names}}\t{{.Status}}" | grep -q "lifepuzzle"; then
    echo ""
    echo -e "${BLUE}📊 Remaining LifePuzzle containers:${NC}"
    docker ps --filter "name=lifepuzzle" --format "table {{.Names}}\t{{.Status}}"
else
    echo -e "${GREEN}✅ No LifePuzzle containers running${NC}"
fi