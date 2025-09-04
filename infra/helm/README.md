# Helm Charts for LifePuzzle Infrastructure

This directory contains Helm charts for managing LifePuzzle infrastructure components.

## Charts

### lifepuzzle-infrastructure

A comprehensive Helm chart that manages MySQL and RabbitMQ infrastructure using Bitnami charts as dependencies.

## Prerequisites

- Helm 3.x
- Kubernetes cluster
- kubectl configured

## Deployment Options

### Full Infrastructure Deployment
Deploy both MySQL and RabbitMQ together:
```bash
./deploy-prod.sh
```

### Individual Component Deployments

#### MySQL Only
```bash
./deploy-mysql-prod.sh
```
Required environment variables:
- `MYSQL_ROOT_PASSWORD`
- `MYSQL_PASSWORD`

#### RabbitMQ Only
```bash
./deploy-rabbitmq-prod.sh
```
Required environment variables:
- `RABBITMQ_PASSWORD`
- `RABBITMQ_ERLANG_COOKIE`

### Environment Variables Setup

#### Option 1: Using .env file (Recommended)
1. Copy the example file:
   ```bash
   cp .env.example .env
   ```
2. Edit `.env` file in the root directory and update the infrastructure variables:
   ```bash
   # Infrastructure Deployment Variables
   MYSQL_ROOT_PASSWORD=your-secure-root-password
   MYSQL_PASSWORD=your-secure-lifepuzzle-password
   RABBITMQ_PASSWORD=your-secure-rabbitmq-password
   RABBITMQ_ERLANG_COOKIE=your-secure-erlang-cookie
   ```
3. Run deployment scripts (they will automatically load the .env file):
   ```bash
   ./deploy-prod.sh
   ```

#### Option 2: Manual export
```bash
# MySQL credentials
export MYSQL_ROOT_PASSWORD='your-secure-root-password'
export MYSQL_PASSWORD='your-secure-lifepuzzle-password'

# RabbitMQ credentials
export RABBITMQ_PASSWORD='your-secure-rabbitmq-password'
export RABBITMQ_ERLANG_COOKIE='your-secure-erlang-cookie'
```

## Helm Releases

| Script | Helm Release Name | Components |
|--------|------------------|------------|
| `deploy-prod.sh` | `lifepuzzle-infra` | MySQL + RabbitMQ |
| `deploy-mysql-prod.sh` | `lifepuzzle-mysql` | MySQL only |
| `deploy-rabbitmq-prod.sh` | `lifepuzzle-rabbitmq` | RabbitMQ only |

## Manual Installation

### 1. Add Bitnami Repository

```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

### 2. Install the Chart

#### Development Environment
```bash
helm install lifepuzzle-infra ./lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --create-namespace \
  --values ./lifepuzzle-infrastructure/values-dev.yaml
```

#### Production Environment
```bash
# Update production passwords first!
helm install lifepuzzle-infra ./lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --create-namespace \
  --values ./lifepuzzle-infrastructure/values-prod.yaml
```

### 3. Verify Installation

```bash
# Check pod status
kubectl get pods -n lifepuzzle

# Check services
kubectl get svc -n lifepuzzle

# Get connection information
helm get notes lifepuzzle-infra -n lifepuzzle
```

## Configuration

### Environment-Specific Values

| Environment | File | Description |
|-------------|------|-------------|
| Development | `values-dev.yaml` | NodePort enabled, reduced resources |
| Production | `values-prod.yaml` | High availability, full resources |

### Key Configuration Options

#### MySQL Configuration
```yaml
mysql:
  enabled: true
  auth:
    rootPassword: "your-root-password"
    database: "lifepuzzle"
    username: "lifepuzzle"
    password: "your-password"
  
  primary:
    persistence:
      size: 10Gi
    resources:
      requests:
        memory: 512Mi
        cpu: 250m
```

#### RabbitMQ Configuration
```yaml
rabbitmq:
  enabled: true
  auth:
    username: "lifepuzzle"
    password: "your-password"
  
  clustering:
    enabled: true
  
  persistence:
    size: 5Gi
```

### Custom Resources

The chart also creates:
- **Namespace**: `lifepuzzle` namespace
- **RabbitMQ Definitions**: Pre-configured queues and exchanges for image processing

## Management Commands

### Upgrade
```bash
helm upgrade lifepuzzle-infra ./lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --values ./lifepuzzle-infrastructure/values-dev.yaml
```

### Rollback
```bash
# List releases
helm history lifepuzzle-infra -n lifepuzzle

# Rollback to previous version
helm rollback lifepuzzle-infra -n lifepuzzle
```

### Uninstall
```bash
helm uninstall lifepuzzle-infra -n lifepuzzle
```

### Status and Debugging
```bash
# Check release status
helm status lifepuzzle-infra -n lifepuzzle

# Get rendered manifests
helm get manifest lifepuzzle-infra -n lifepuzzle

# Get values
helm get values lifepuzzle-infra -n lifepuzzle
```

## Accessing Services

### MySQL

#### Internal Access (from within cluster)
```
Host: lifepuzzle-infra-mysql.lifepuzzle.svc.cluster.local
Port: 3306
Database: lifepuzzle
Username: lifepuzzle
```

#### External Access (Development)
When NodePort is enabled:
```bash
# Get NodePort
kubectl get svc lifepuzzle-infra-mysql -n lifepuzzle

# Connect
mysql -h <NODE_IP> -P 30306 -u lifepuzzle -p
```

### RabbitMQ

#### Internal Access (from within cluster)
```
AMQP: lifepuzzle-infra-rabbitmq.lifepuzzle.svc.cluster.local:5672
Management: lifepuzzle-infra-rabbitmq.lifepuzzle.svc.cluster.local:15672
Username: lifepuzzle
Virtual Host: lifepuzzle
```

#### Management UI Access
```bash
# Port forward (all environments)
kubectl port-forward svc/lifepuzzle-infra-rabbitmq 15672:15672 -n lifepuzzle

# Or use NodePort (development only)
# Visit http://<NODE_IP>:31672
```

## Environment Variables for Applications

Use these environment variables in your application deployments:

```yaml
env:
  # Database
  - name: DB_HOST
    value: lifepuzzle-infra-mysql.lifepuzzle.svc.cluster.local
  - name: DB_PORT
    value: "3306"
  - name: DB_NAME
    value: lifepuzzle
  - name: DB_USER
    value: lifepuzzle
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: lifepuzzle-infra-mysql
        key: mysql-password
  
  # RabbitMQ
  - name: RABBITMQ_HOST
    value: lifepuzzle-infra-rabbitmq.lifepuzzle.svc.cluster.local
  - name: RABBITMQ_PORT
    value: "5672"
  - name: RABBITMQ_USER
    value: lifepuzzle
  - name: RABBITMQ_PASSWORD
    valueFrom:
      secretKeyRef:
        name: lifepuzzle-infra-rabbitmq
        key: rabbitmq-password
  - name: RABBITMQ_VHOST
    value: lifepuzzle
```

## Security Considerations

### Production Security

1. **Change Default Passwords**: Update all passwords in production values
2. **External Secret Management**: Consider using:
   - [External Secrets Operator](https://external-secrets.io/)
   - [Helm Secrets](https://github.com/jkroepke/helm-secrets)
   - Cloud provider secret managers (AWS Secrets Manager, etc.)

3. **Network Policies**: Implement network policies to restrict access
4. **RBAC**: Configure proper role-based access control
5. **TLS**: Enable TLS for RabbitMQ in production

### Example with External Secrets
```bash
# Install with external secret references
helm install lifepuzzle-infra ./lifepuzzle-infrastructure \
  --namespace lifepuzzle \
  --create-namespace \
  --set mysql.auth.existingSecret=mysql-external-secret \
  --set rabbitmq.auth.existingSecret=rabbitmq-external-secret
```

## Monitoring and Observability

### Metrics Collection
- RabbitMQ metrics can be enabled via `rabbitmq.metrics.enabled`
- Prometheus ServiceMonitor can be created for scraping
- MySQL metrics require additional exporters

### Logging
```bash
# View MySQL logs
kubectl logs -f deployment/lifepuzzle-infra-mysql -n lifepuzzle

# View RabbitMQ logs
kubectl logs -f statefulset/lifepuzzle-infra-rabbitmq -n lifepuzzle
```

### Health Checks
Both services include comprehensive health checks:
- Liveness probes to restart unhealthy containers
- Readiness probes to control traffic routing

## Backup and Recovery

### MySQL Backup
```bash
# Manual backup
kubectl exec -it deployment/lifepuzzle-infra-mysql -n lifepuzzle -- \
  mysqldump -u root -p lifepuzzle > backup.sql

# Restore
kubectl exec -i deployment/lifepuzzle-infra-mysql -n lifepuzzle -- \
  mysql -u root -p lifepuzzle < backup.sql
```

### RabbitMQ Backup
```bash
# Export definitions
kubectl exec -it statefulset/lifepuzzle-infra-rabbitmq -n lifepuzzle -- \
  rabbitmqctl export_definitions /tmp/definitions.json

# Copy from pod
kubectl cp lifepuzzle/lifepuzzle-infra-rabbitmq-0:/tmp/definitions.json ./definitions.json
```

## Troubleshooting

### Common Issues

1. **Pods stuck in Pending**: Check storage class and available resources
2. **Connection refused**: Verify service names and ports
3. **Authentication failed**: Check secret values and configuration

### Debug Commands
```bash
# Describe resources
kubectl describe pod <pod-name> -n lifepuzzle
kubectl describe pvc <pvc-name> -n lifepuzzle

# Check events
kubectl get events -n lifepuzzle --sort-by=.metadata.creationTimestamp

# Shell into containers
kubectl exec -it deployment/lifepuzzle-infra-mysql -n lifepuzzle -- bash
kubectl exec -it statefulset/lifepuzzle-infra-rabbitmq-0 -n lifepuzzle -- bash
```

## Migration from Raw Manifests

If migrating from the raw Kubernetes manifests in `infra/k8s/`, follow these steps:

1. **Backup existing data** if any
2. **Delete old resources**:
   ```bash
   kubectl delete -k infra/k8s/
   ```
3. **Install Helm chart**:
   ```bash
   helm install lifepuzzle-infra ./lifepuzzle-infrastructure \
     --namespace lifepuzzle \
     --values ./lifepuzzle-infrastructure/values-dev.yaml
   ```

## Contributing

When modifying the chart:
1. Update version in `Chart.yaml`
2. Test with `helm template` and `helm lint`
3. Validate with different value files
4. Update documentation

## Support

For issues and questions:
- Check the troubleshooting section
- Review Helm and Kubernetes logs
- Consult Bitnami chart documentation for MySQL and RabbitMQ specific issues