#!/bin/bash

# Cleanup LifePuzzle Docker resources
# This script removes containers, images, volumes, and networks

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}🧹 LifePuzzle Cleanup Script${NC}"
echo "============================"

# Function to confirm action
confirm() {
    local message=$1
    echo -e "${YELLOW}⚠️  $message${NC}"
    read -p "Are you sure? (y/N): " -n 1 -r
    echo
    [[ $REPLY =~ ^[Yy]$ ]]
}

# Navigate to docker directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DOCKER_DIR="$SCRIPT_DIR/../../infra/docker"

cd "$DOCKER_DIR"

echo "This script will help you clean up LifePuzzle Docker resources."
echo "Choose what you want to clean up:"
echo ""
echo "1) Stop and remove containers only"
echo "2) Remove containers and images"
echo "3) Remove containers, images, and volumes (⚠️  DATA LOSS)"
echo "4) Full cleanup including networks (⚠️  DATA LOSS)"
echo "5) Cancel"
echo ""

read -p "Enter your choice (1-5): " choice

case $choice in
    1)
        if confirm "This will stop and remove all LifePuzzle containers."; then
            echo -e "${BLUE}🛑 Stopping and removing containers...${NC}"
            
            # Stop services
            docker-compose down 2>/dev/null || true
            docker-compose -f docker-compose.full.yml down 2>/dev/null || true
            
            # Remove any remaining LifePuzzle containers
            if docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}" | grep -q "lifepuzzle"; then
                docker rm -f $(docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}")
            fi
            
            echo -e "${GREEN}✅ Containers removed${NC}"
        fi
        ;;
        
    2)
        if confirm "This will remove containers and locally built images."; then
            echo -e "${BLUE}🛑 Stopping and removing containers...${NC}"
            
            # Stop services
            docker-compose down 2>/dev/null || true
            docker-compose -f docker-compose.full.yml down 2>/dev/null || true
            
            # Remove containers
            if docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}" | grep -q "lifepuzzle"; then
                docker rm -f $(docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}")
            fi
            
            echo -e "${BLUE}🗑️  Removing locally built images...${NC}"
            
            # Remove locally built images
            if docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}" | grep -q "lifepuzzle"; then
                docker rmi $(docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}")
            fi
            
            echo -e "${GREEN}✅ Containers and images removed${NC}"
        fi
        ;;
        
    3)
        if confirm "This will remove containers, images, and volumes. ALL DATABASE DATA WILL BE LOST!"; then
            echo -e "${BLUE}🛑 Stopping and removing containers...${NC}"
            
            # Stop services with volume removal
            docker-compose down -v 2>/dev/null || true
            docker-compose -f docker-compose.full.yml down -v 2>/dev/null || true
            
            # Remove containers
            if docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}" | grep -q "lifepuzzle"; then
                docker rm -f $(docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}")
            fi
            
            echo -e "${BLUE}🗑️  Removing images...${NC}"
            if docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}" | grep -q "lifepuzzle"; then
                docker rmi $(docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}")
            fi
            
            echo -e "${BLUE}🗑️  Removing volumes...${NC}"
            if docker volume ls --filter "name=docker_mysql_data" --format "{{.Name}}" | grep -q "mysql_data"; then
                docker volume rm $(docker volume ls --filter "name=docker_mysql_data" --format "{{.Name}}")
            fi
            if docker volume ls --filter "name=docker_rabbitmq_data" --format "{{.Name}}" | grep -q "rabbitmq_data"; then
                docker volume rm $(docker volume ls --filter "name=docker_rabbitmq_data" --format "{{.Name}}")
            fi
            
            echo -e "${GREEN}✅ Containers, images, and volumes removed${NC}"
        fi
        ;;
        
    4)
        if confirm "This will perform FULL cleanup including networks. ALL DATA WILL BE LOST!"; then
            echo -e "${BLUE}🛑 Full cleanup...${NC}"
            
            # Stop services with volume removal
            docker-compose down -v --remove-orphans 2>/dev/null || true
            docker-compose -f docker-compose.full.yml down -v --remove-orphans 2>/dev/null || true
            
            # Remove all LifePuzzle containers
            if docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}" | grep -q "lifepuzzle"; then
                docker rm -f $(docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}")
            fi
            
            # Remove images
            if docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}" | grep -q "lifepuzzle"; then
                docker rmi $(docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}:{{.Tag}}")
            fi
            
            # Remove volumes
            if docker volume ls --filter "name=docker_mysql_data" --format "{{.Name}}" | grep -q "mysql_data"; then
                docker volume rm $(docker volume ls --filter "name=docker_mysql_data" --format "{{.Name}}")
            fi
            if docker volume ls --filter "name=docker_rabbitmq_data" --format "{{.Name}}" | grep -q "rabbitmq_data"; then
                docker volume rm $(docker volume ls --filter "name=docker_rabbitmq_data" --format "{{.Name}}")
            fi
            
            # Remove networks
            if docker network ls --filter "name=docker_lifepuzzle-network" --format "{{.Name}}" | grep -q "lifepuzzle-network"; then
                docker network rm $(docker network ls --filter "name=docker_lifepuzzle-network" --format "{{.Name}}")
            fi
            
            echo -e "${GREEN}✅ Full cleanup completed${NC}"
        fi
        ;;
        
    5)
        echo "Cleanup cancelled."
        exit 0
        ;;
        
    *)
        echo "Invalid choice. Exiting."
        exit 1
        ;;
esac

echo ""
echo -e "${BLUE}📊 Remaining LifePuzzle resources:${NC}"

# Show remaining containers
if docker ps -a --filter "name=lifepuzzle" --format "{{.Names}}" | grep -q "lifepuzzle"; then
    echo "Containers:"
    docker ps -a --filter "name=lifepuzzle" --format "table {{.Names}}\t{{.Status}}"
else
    echo "Containers: None"
fi

# Show remaining images
if docker images --filter "reference=*lifepuzzle*" --format "{{.Repository}}" | grep -q "lifepuzzle"; then
    echo "Images:"
    docker images --filter "reference=*lifepuzzle*" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"
else
    echo "Images: None"
fi

# Show remaining volumes
if docker volume ls --filter "name=docker" --format "{{.Name}}" | grep -E "(mysql_data|rabbitmq_data)"; then
    echo "Volumes:"
    docker volume ls --filter "name=docker" --format "table {{.Name}}\t{{.Driver}}" | grep -E "(mysql_data|rabbitmq_data)"
else
    echo "Volumes: None"
fi

echo ""
echo -e "${GREEN}🎉 Cleanup completed!${NC}"
echo -e "${BLUE}💡 To set up again: ./tools/scripts/setup-dev.sh${NC}"