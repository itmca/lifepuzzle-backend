#!/bin/bash

# MySQL-only production deployment script for LifePuzzle Infrastructure
# Usage: ./deploy-mysql-prod.sh

set -e

# Load environment variables from root .env file if it exists
ROOT_ENV_FILE="$(cd "$(dirname "$0")/../.." && pwd)/.env"
if [ -f "$ROOT_ENV_FILE" ]; then
    echo "📁 Loading environment variables from .env file..."
    export $(cat "$ROOT_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

echo "🗄️ Deploying MySQL to Production..."

# Check if required environment variables are set
required_vars=(
    "MYSQL_ROOT_PASSWORD"
    "MYSQL_PASSWORD"
)

echo "🔍 Checking environment variables..."
for var in "${required_vars[@]}"; do
    if [[ -z "${!var}" ]]; then
        echo "❌ Error: Environment variable $var is not set"
        echo "Please set all required variables:"
        echo "  export MYSQL_ROOT_PASSWORD='your-secure-root-password'"
        echo "  export MYSQL_PASSWORD='your-secure-lifepuzzle-password'"
        exit 1
    fi
    echo "✅ $var is set"
done

# Add Bitnami repository if not already added
echo "📦 Adding Bitnami Helm repository..."
helm repo add bitnami https://charts.bitnami.com/bitnami || true
helm repo update

# Deploy or upgrade MySQL only
echo "🔧 Deploying MySQL..."
helm upgrade --install lifepuzzle-mysql ./lifepuzzle-infrastructure \
    --namespace lifepuzzle \
    --create-namespace \
    --values ./lifepuzzle-infrastructure/values-prod.yaml \
    --set mysql.enabled=true \
    --set rabbitmq.enabled=false \
    --set mysql.auth.rootPassword="$MYSQL_ROOT_PASSWORD" \
    --set mysql.auth.password="$MYSQL_PASSWORD" \
    --wait \
    --timeout=10m

echo "✅ MySQL deployment completed successfully!"

# Show deployment status
echo "📊 MySQL Deployment Status:"
helm status lifepuzzle-mysql -n lifepuzzle

echo ""
echo "🔍 MySQL Pod Status:"
kubectl get pods -n lifepuzzle -l app.kubernetes.io/component=primary

echo ""
echo "🌐 MySQL Service Status:"
kubectl get svc -n lifepuzzle -l app.kubernetes.io/name=mysql

echo ""
echo "💡 Next Steps:"
echo "1. Verify MySQL pod is running: kubectl get pods -n lifepuzzle -l app.kubernetes.io/name=mysql"
echo "2. Check MySQL logs: kubectl logs -f <mysql-pod-name> -n lifepuzzle"
echo "3. Connect to MySQL:"
echo "   kubectl port-forward svc/lifepuzzle-mysql 3306:3306 -n lifepuzzle"