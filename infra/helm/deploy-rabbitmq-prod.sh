#!/bin/bash

# RabbitMQ-only production deployment script for LifePuzzle Infrastructure
# Usage: ./deploy-rabbitmq-prod.sh

set -e

# Load environment variables from root .env file if it exists
ROOT_ENV_FILE="$(cd "$(dirname "$0")/../.." && pwd)/.env"
if [ -f "$ROOT_ENV_FILE" ]; then
    echo "📁 Loading environment variables from .env file..."
    export $(cat "$ROOT_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

echo "🐰 Deploying RabbitMQ to Production..."

# Check if required environment variables are set
required_vars=(
    "RABBITMQ_PASSWORD"
    "RABBITMQ_ERLANG_COOKIE"
)

echo "🔍 Checking environment variables..."
for var in "${required_vars[@]}"; do
    if [[ -z "${!var}" ]]; then
        echo "❌ Error: Environment variable $var is not set"
        echo "Please set all required variables:"
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

# Deploy or upgrade RabbitMQ only
echo "🔧 Deploying RabbitMQ..."
helm upgrade --install lifepuzzle-rabbitmq ./lifepuzzle-infrastructure \
    --namespace lifepuzzle \
    --create-namespace \
    --values ./lifepuzzle-infrastructure/values-prod.yaml \
    --set mysql.enabled=false \
    --set rabbitmq.enabled=true \
    --set rabbitmq.auth.password="$RABBITMQ_PASSWORD" \
    --set rabbitmq.auth.erlangCookie="$RABBITMQ_ERLANG_COOKIE" \
    --set rabbitmq.auth.vhost="$RABBITMQ_DEFAULT_VHOST" \
    --wait \
    --timeout=10m

echo "✅ RabbitMQ deployment completed successfully!"

# Show deployment status
echo "📊 RabbitMQ Deployment Status:"
helm status lifepuzzle-rabbitmq -n lifepuzzle

echo ""
echo "🔍 RabbitMQ Pod Status:"
kubectl get pods -n lifepuzzle -l app.kubernetes.io/name=rabbitmq

echo ""
echo "🌐 RabbitMQ Service Status:"
kubectl get svc -n lifepuzzle -l app.kubernetes.io/name=rabbitmq

echo ""
echo "💡 Next Steps:"
echo "1. Verify RabbitMQ pod is running: kubectl get pods -n lifepuzzle -l app.kubernetes.io/name=rabbitmq"
echo "2. Check RabbitMQ logs: kubectl logs -f <rabbitmq-pod-name> -n lifepuzzle"
echo "3. Access RabbitMQ Management UI:"
echo "   kubectl port-forward svc/lifepuzzle-rabbitmq 15672:15672 -n lifepuzzle"
echo "   Then open http://localhost:15672 (user: user, password: \$RABBITMQ_PASSWORD)"