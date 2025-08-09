#!/bin/bash

# Production deployment script for LifePuzzle Image Resizer
# Usage: ./deploy-image-resizer-prod.sh

set -e

# Load environment variables from root .env file if it exists
ROOT_ENV_FILE="$(cd "$(dirname "$0")/../.." && pwd)/.env"
if [ -f "$ROOT_ENV_FILE" ]; then
    echo "📁 Loading environment variables from .env file..."
    export $(cat "$ROOT_ENV_FILE" | grep -v '^#' | grep -v '^$' | xargs)
fi

echo "🚀 Deploying LifePuzzle Image Resizer to Production..."

# Check if required environment variables are set
required_vars=(
    "MYSQL_PASSWORD"
    "RABBITMQ_PASSWORD"
    "AWS_ACCESS_KEY_ID"
    "AWS_SECRET_ACCESS_KEY"
    "S3_BUCKET_NAME"
)

echo "🔍 Checking environment variables..."
for var in "${required_vars[@]}"; do
    if [[ -z "${!var}" ]]; then
        echo "❌ Error: Environment variable $var is not set"
        echo "Please set all required variables:"
        echo "  export MYSQL_PASSWORD='your-mysql-password'"
        echo "  export RABBITMQ_PASSWORD='your-rabbitmq-password'"
        echo "  export AWS_ACCESS_KEY_ID='your-aws-access-key'"
        echo "  export AWS_SECRET_ACCESS_KEY='your-aws-secret-key'"
        echo "  export S3_BUCKET_NAME='your-s3-bucket-name'"
        exit 1
    fi
    echo "✅ $var is set"
done

# Deploy or upgrade the image resizer
echo "🔧 Deploying image resizer..."
helm upgrade --install lifepuzzle-image-resizer ./lifepuzzle-image-resizer \
    --namespace lifepuzzle \
    --create-namespace \
    --values ./lifepuzzle-image-resizer/values-prod.yaml \
    --set secrets.dbPassword="$MYSQL_PASSWORD" \
    --set secrets.rabbitmqPassword="$RABBITMQ_PASSWORD" \
    --set secrets.awsAccessKeyId="$AWS_ACCESS_KEY_ID" \
    --set secrets.awsSecretAccessKey="$AWS_SECRET_ACCESS_KEY" \
    --set secrets.s3BucketName="$S3_BUCKET_NAME" \
    --wait \
    --timeout=10m

echo "✅ Deployment completed successfully!"

# Show deployment status
echo "📊 Deployment Status:"
helm status lifepuzzle-image-resizer -n lifepuzzle

echo ""
echo "🔍 Pod Status:"
kubectl get pods -n lifepuzzle -l app.kubernetes.io/name=lifepuzzle-image-resizer

echo ""
echo "🌐 Service Status:"
kubectl get svc -n lifepuzzle -l app.kubernetes.io/name=lifepuzzle-image-resizer

echo ""
echo "📈 HPA Status:"
kubectl get hpa -n lifepuzzle

echo ""
echo "💡 Next Steps:"
echo "1. Verify all pods are running: kubectl get pods -n lifepuzzle"
echo "2. Check logs: kubectl logs -f deployment/lifepuzzle-image-resizer -n lifepuzzle"
echo "3. Monitor HPA scaling: kubectl get hpa -w -n lifepuzzle"
echo "4. Test health endpoint:"
echo "   kubectl port-forward svc/lifepuzzle-image-resizer 9000:9000 -n lifepuzzle"
echo "   curl http://localhost:9000/health"