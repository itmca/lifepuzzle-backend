# image-resizer

Go-based microservice for asynchronous image processing and resizing.

## Project Structure

```bash
image-resizer/
├── main.go                           # Application entry point
├── config/
│   └── config.go                     # Configuration management
├── database/
│   └── database.go                   # Database connection & operations
├── messaging/
│   └── rabbitmq.go                   # RabbitMQ message handling
├── resizer/
│   └── resizer.go                    # Image resizing logic
├── storage/
│   └── s3.go                         # S3 storage operations
├── k8s-mysql.yaml                    # Kubernetes MySQL deployment
├── k8s-rabbitmq.yaml                # Kubernetes RabbitMQ deployment
├── Dockerfile                        # Container image build
├── Dockerfile.standalone             # Standalone container build
├── go.mod                           # Go module dependencies
└── go.sum                           # Go module checksums
```

## Architecture

The image-resizer service follows a message-driven architecture:

1. **Message Queue**: Receives resize requests via RabbitMQ
2. **Image Processing**: Downloads, resizes, and optimizes images
3. **Storage**: Uploads processed images to S3
4. **Database**: Updates metadata and status

## Features

- **Asynchronous Processing**: Queue-based image processing
- **Multiple Formats**: Support for various image formats
- **Size Optimization**: Automatic image compression and optimization
- **Scalable**: Kubernetes-ready with horizontal scaling
- **Monitoring**: Health checks and processing metrics

## Technology Stack

- **Language**: Go 1.24+
- **Message Queue**: RabbitMQ
- **Storage**: AWS S3
- **Database**: MySQL (shared with lifepuzzle-api)
- **Container**: Docker
- **Orchestration**: Kubernetes

## Configuration

The service is configured via environment variables:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=lifepuzzle
DB_USER=user
DB_PASSWORD=password

# RabbitMQ
RABBITMQ_URL=amqp://guest:guest@localhost:5672/

# AWS S3
AWS_REGION=us-west-2
AWS_ACCESS_KEY_ID=your-key
AWS_SECRET_ACCESS_KEY=your-secret
S3_BUCKET=your-bucket

# Application
PORT=8080
LOG_LEVEL=info
```

## Getting Started

### Prerequisites

- Go 1.24+
- RabbitMQ
- MySQL (shared with lifepuzzle-api)
- AWS S3 bucket and credentials

### Local Development

```bash
# Install dependencies
go mod download

# Run the service
go run main.go

# Build binary
go build -o image-resizer main.go
```

### Docker

```bash
# Build image
docker build -t image-resizer .

# Run container
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e RABBITMQ_URL=amqp://guest:guest@host.docker.internal:5672/ \
  image-resizer
```

### Kubernetes Deployment

The service includes Kubernetes manifests for dependencies:

```bash
# Deploy MySQL
kubectl apply -f k8s-mysql.yaml

# Deploy RabbitMQ
kubectl apply -f k8s-rabbitmq.yaml

# Deploy the service (use your own deployment manifest)
kubectl apply -f your-deployment.yaml
```

## API Endpoints

### Health Check

```http
GET /health
```

Returns service health status and dependency connectivity.

### Metrics

```http
GET /metrics
```

Returns processing metrics and queue status.

## Message Format

The service processes messages with the following JSON format:

```json
{
  "image_url": "https://example.com/original.jpg",
  "target_sizes": [
    {"width": 300, "height": 300, "quality": 80},
    {"width": 800, "height": 600, "quality": 90}
  ],
  "output_format": "jpeg",
  "callback_url": "https://api.example.com/callback"
}
```

## Monitoring

The service provides:
- Health check endpoint for liveness/readiness probes
- Metrics for processing performance
- Structured logging for debugging
- Error tracking and alerting

## Scaling

The service is designed for horizontal scaling:
- Stateless processing
- Queue-based work distribution
- Kubernetes-ready configuration
- Resource-efficient Go runtime