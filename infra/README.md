# Infrastructure Configuration

This directory contains infrastructure configuration files for the LifePuzzle backend services.

## Structure

```bash
infra/
├── docker/                       # Docker Compose setup
│   ├── docker-compose.yml        # Main compose file
│   ├── .env                      # Environment variables
│   ├── mysql/
│   │   └── init/
│   │       └── 01-init.sql       # MySQL initialization script
│   └── rabbitmq/
│       ├── rabbitmq.conf         # RabbitMQ configuration
│       └── definitions.json      # Queue/Exchange definitions
└── helm/                         # Helm charts
    ├── lifepuzzle-infrastructure/ # Main infrastructure chart
    ├── deploy-prod.sh            # Production deployment script
    └── README.md                 # Helm documentation
```

## Docker Compose

Two Docker Compose configurations are available for different use cases:

### Option 1: Infrastructure Only (Recommended for Backend Development)

Use this when you want to run applications separately (e.g., in IDE for debugging):

```bash
cd infra/docker
docker-compose up -d
```

**Services:**
- **MySQL**: `localhost:3306`
- **RabbitMQ**: 
  - AMQP: `localhost:5672`
  - Management UI: `http://localhost:15672`

### Option 2: Full Application Stack (Recommended for Frontend Development)

Use this when you want everything running together for frontend testing:

```bash
cd infra/docker

# Copy environment file and update AWS credentials
cp .env.example .env
# Edit .env file to set your AWS credentials

# Start full stack
docker-compose -f docker-compose.full.yml up -d
```

**Services:**
- **MySQL**: `localhost:3306`
- **RabbitMQ**: 
  - AMQP: `localhost:5672`
  - Management UI: `http://localhost:15672`
- **LifePuzzle API**: `http://localhost:8080`
  - Health check: `http://localhost:8080/actuator/health`
- **Image Resizer**: `http://localhost:9000`
  - Health check: `http://localhost:9000/health`

### Environment Configuration

Copy and customize the environment file:
```bash
cp .env.example .env
```

**Required for Full Stack:**
- Set your AWS credentials in `.env` file for image-resizer service
- AWS S3 bucket for image storage

### Default Credentials

- **MySQL**:
  - Root password: `rootpassword`
  - Database: `lifepuzzle`
  - User: `lifepuzzle`
  - Password: `lifepuzzlepass`

- **RabbitMQ**:
  - User: `lifepuzzle`
  - Password: `lifepuzzlepass`
  - Virtual Host: `lifepuzzle`

### Usage Examples

#### Using Scripts (Recommended)

**For Frontend Developers:**
```bash
# First time setup
./tools/scripts/setup-dev.sh

# Daily usage
./tools/scripts/start-full.sh    # Start all services
./tools/scripts/health.sh        # Check status
./tools/scripts/logs.sh api      # View API logs
./tools/scripts/stop.sh          # Stop all services
```

**For Backend Developers:**
```bash
./tools/scripts/start-infra.sh   # Start only MySQL + RabbitMQ
# Then run your apps in IDE
./tools/scripts/stop.sh          # Stop when done
```

See [tools/scripts/README.md](../tools/scripts/README.md) for detailed script documentation.

#### Manual Docker Compose

```bash
# Infrastructure only (backend development)
cd infra/docker
docker-compose up -d

# Full stack (frontend development)
cd infra/docker
cp .env.example .env  # Edit AWS credentials
docker-compose -f docker-compose.full.yml up -d

# View logs
docker-compose logs -f lifepuzzle-api
docker-compose logs -f image-resizer

# Stop services
docker-compose down
docker-compose -f docker-compose.full.yml down

# Rebuild and restart
docker-compose -f docker-compose.full.yml up -d --build
```

## Kubernetes

Using **Helm charts** for better management, templating, and versioning.

### Helm Charts

See [helm/README.md](./helm/README.md) for comprehensive Helm chart documentation.

#### Development Environment
```bash
# Add Bitnami repository
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update

# Install for development
helm install lifepuzzle-infra ./helm/lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --create-namespace \
  --values ./helm/lifepuzzle-infrastructure/values-dev.yaml
```

#### Production Environment

**Using Environment Variables (Recommended):**
```bash
# Set environment variables for sensitive data
export MYSQL_ROOT_PASSWORD="your-secure-root-password"
export MYSQL_PASSWORD="your-secure-lifepuzzle-password"
export RABBITMQ_PASSWORD="your-secure-rabbitmq-password"
export RABBITMQ_ERLANG_COOKIE="your-secure-erlang-cookie"

# Deploy using the script
cd infra/helm
./deploy-prod.sh
```

**Manual Deployment:**
```bash
helm install lifepuzzle-infra ./helm/lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --create-namespace \
  --values ./helm/lifepuzzle-infrastructure/values-prod.yaml \
  --set mysql.auth.rootPassword="$MYSQL_ROOT_PASSWORD" \
  --set mysql.auth.password="$MYSQL_PASSWORD" \
  --set rabbitmq.auth.password="$RABBITMQ_PASSWORD" \
  --set rabbitmq.auth.erlangCookie="$RABBITMQ_ERLANG_COOKIE"
```

### Access Services

#### MySQL
```bash
# Internal access (from within cluster)
mysql-service.lifepuzzle.svc.cluster.local:3306

# External access (development only)
kubectl port-forward svc/mysql-nodeport 3306:3306 -n lifepuzzle
```

#### RabbitMQ
```bash
# Internal access (from within cluster)
rabbitmq-service.lifepuzzle.svc.cluster.local:5672

# Management UI (development only)
kubectl port-forward svc/rabbitmq-nodeport 15672:15672 -n lifepuzzle
# Then visit: http://localhost:15672
```

### Configuration

#### MySQL
- Configured with UTF-8MB4 charset
- Korean timezone (+09:00)
- Performance optimizations for containerized environment
- Automatic database initialization

#### RabbitMQ
- Pre-configured with image processing queues
- Management plugin enabled
- Kubernetes peer discovery for clustering
- Persistent storage for durability

### Scaling

#### MySQL
- Currently configured as single instance
- For high availability, consider MySQL InnoDB Cluster or external managed database

#### RabbitMQ
- Configured as StatefulSet for easy scaling
- To scale: `kubectl scale statefulset rabbitmq --replicas=3 -n lifepuzzle`
- Automatic clustering with Kubernetes peer discovery

### Security Notes

- Default passwords are used for development
- For production, update secrets with strong passwords
- Consider using external secret management (e.g., Vault, AWS Secrets Manager)
- Network policies should be implemented to restrict access

### Monitoring

- Health checks configured for both services
- Resource limits set to prevent resource exhaustion
- Logs available via `kubectl logs`

### Backup

- MySQL data is persisted in PVC
- RabbitMQ data and configuration are persisted
- Implement regular backup strategies for production

## Environment Variables

The following environment variables should be set in your application:

```bash
# Database
DB_HOST=mysql-service.lifepuzzle.svc.cluster.local  # or localhost for Docker
DB_PORT=3306
DB_NAME=lifepuzzle
DB_USER=lifepuzzle
DB_PASSWORD=lifepuzzlepass

# RabbitMQ
RABBITMQ_HOST=rabbitmq-service.lifepuzzle.svc.cluster.local  # or localhost for Docker
RABBITMQ_PORT=5672
RABBITMQ_USER=lifepuzzle
RABBITMQ_PASSWORD=lifepuzzlepass
RABBITMQ_VHOST=lifepuzzle
```