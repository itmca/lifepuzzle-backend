#!/bin/bash

# Full infrastructure production deployment script for LifePuzzle Infrastructure
# This deploys both MySQL and RabbitMQ together
# Usage: ./deploy-prod.sh
# 
# For individual deployments, use:
# - ./deploy-mysql-prod.sh (MySQL only)
# - ./deploy-rabbitmq-prod.sh (RabbitMQ only)

set -e

# Load environment variables from root .env file if it exists
ROOT_ENV_FILE="$(cd "$(dirname "$0")/../.." && pwd)/.env"
if [ -f "$ROOT_ENV_FILE" ]; then
    echo "📁 Loading environment variables from .env file..."
    export $(cat "$ROOT_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

echo "🚀 Deploying Full LifePuzzle Infrastructure to Production..."

# Check if required environment variables are set
required_vars=(
    "MYSQL_ROOT_PASSWORD"
    "MYSQL_PASSWORD" 
    "RABBITMQ_PASSWORD"
    "RABBITMQ_ERLANG_COOKIE"
)

echo "🔍 Checking environment variables..."
for var in "${required_vars[@]}"; do
    if [[ -z "${!var}" ]]; then
        echo "❌ Error: Environment variable $var is not set"
        echo "Please set all required variables:"
        echo "  export MYSQL_ROOT_PASSWORD='your-secure-root-password'"
        echo "  export MYSQL_PASSWORD='your-secure-lifepuzzle-password'"
        echo "  export RABBITMQ_PASSWORD='your-secure-rabbitmq-password'"
        echo "  export RABBITMQ_ERLANG_COOKIE='your-secure-erlang-cookie'"
        exit 1
    fi
    echo "✅ $var is set"
done

# Add Bitnami repository if not already added
echo "📦 Adding Bitnami Helm repository..."
helm repo add bitnami https://charts.bitnami.com/bitnami || true
helm repo update

# Build dependencies
echo "🔧 Building Helm chart dependencies..."
helm dependency build ./lifepuzzle-infrastructure

# Deploy or upgrade the infrastructure
echo "🔧 Deploying infrastructure..."
helm upgrade --install lifepuzzle-infra ./lifepuzzle-infrastructure \
    --namespace lifepuzzle \
    --create-namespace \
    --values ./lifepuzzle-infrastructure/values-prod.yaml \
    --set mysql.auth.rootPassword="$MYSQL_ROOT_PASSWORD" \
    --set mysql.auth.password="$MYSQL_PASSWORD" \
    --set rabbitmq.auth.password="$RABBITMQ_PASSWORD" \
    --set rabbitmq.auth.erlangCookie="$RABBITMQ_ERLANG_COOKIE" \
    --set rabbitmq.auth.vhost="$RABBITMQ_DEFAULT_VHOST" \
    --set rabbitmq.metrics.enabled=false \
    --set rabbitmq.metrics.serviceMonitor.enabled=false \
    --set rabbitmq.metrics.prometheusRule.enabled=false \
    --wait \
    --timeout=10m

echo "✅ Deployment completed successfully!"

# Show deployment status
echo "📊 Deployment Status:"
helm status lifepuzzle-infra -n lifepuzzle

echo ""
echo "🔍 Pod Status:"
kubectl get pods -n lifepuzzle

echo ""
echo "🌐 Service Status:"
kubectl get svc -n lifepuzzle

echo ""
echo "💡 Next Steps:"
echo "1. Verify all pods are running: kubectl get pods -n lifepuzzle"
echo "2. Check logs if needed: kubectl logs -f <pod-name> -n lifepuzzle"
echo "3. Access RabbitMQ Management UI via port-forward:"
echo "   kubectl port-forward svc/lifepuzzle-infra-rabbitmq 15672:15672 -n lifepuzzle"