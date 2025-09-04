#!/bin/bash

# View logs for LifePuzzle services

set -e

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

cd "$DOCKER_DIR"

# Function to show usage
show_usage() {
    echo "Usage: $0 [service] [options]"
    echo ""
    echo "Services:"
    echo "  api         - LifePuzzle API logs"
    echo "  resizer     - Image Resizer logs"
    echo "  mysql       - MySQL logs"
    echo "  rabbitmq    - RabbitMQ logs"
    echo "  all         - All services logs (default)"
    echo ""
    echo "Options:"
    echo "  -f, --follow    Follow log output"
    echo "  -t, --tail N    Number of lines to show (default: 100)"
    echo ""
    echo "Examples:"
    echo "  $0                    # Show last 100 lines of all services"
    echo "  $0 api -f             # Follow API logs"
    echo "  $0 mysql -t 50        # Show last 50 lines of MySQL logs"
}

# Default values
SERVICE="all"
FOLLOW=false
TAIL=100

# Parse arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        api|resizer|mysql|rabbitmq|all)
            SERVICE="$1"
            shift
            ;;
        -f|--follow)
            FOLLOW=true
            shift
            ;;
        -t|--tail)
            TAIL="$2"
            shift 2
            ;;
        -h|--help)
            show_usage
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            show_usage
            exit 1
            ;;
    esac
done

# Build docker-compose command
COMPOSE_CMD="docker-compose"
if docker-compose -f docker-compose.full.yml ps lifepuzzle-api 2>/dev/null | grep -q "Up"; then
    COMPOSE_CMD="docker-compose -f docker-compose.full.yml"
fi

# Build log command
LOG_CMD="$COMPOSE_CMD logs --tail=$TAIL"
if [ "$FOLLOW" = true ]; then
    LOG_CMD="$LOG_CMD -f"
fi

# Execute based on service
case $SERVICE in
    api)
        echo -e "${BLUE}📱 LifePuzzle API Logs${NC}"
        $LOG_CMD lifepuzzle-api
        ;;
    resizer)
        echo -e "${BLUE}🖼️  Image Resizer Logs${NC}"
        $LOG_CMD image-resizer
        ;;
    mysql)
        echo -e "${BLUE}🗄️  MySQL Logs${NC}"
        $LOG_CMD mysql
        ;;
    rabbitmq)
        echo -e "${BLUE}🐰 RabbitMQ Logs${NC}"
        $LOG_CMD rabbitmq
        ;;
    all)
        echo -e "${BLUE}📋 All Services Logs${NC}"
        $LOG_CMD
        ;;
esac