FROM golang:1.21-alpine AS builder

WORKDIR /app

# Copy go workspace files
COPY go.work go.work.sum* ./
COPY shared/go-common ./shared/go-common

# Copy service files
COPY services/image-resizer ./services/image-resizer

WORKDIR /app/services/image-resizer

RUN go mod download
RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix nocgo -o main .

FROM alpine:latest

RUN apk --no-cache add ca-certificates wget

WORKDIR /root/

# Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

COPY --from=builder --chown=appuser:appgroup /app/services/image-resizer/main .

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8081/health || exit 1

CMD ["./main"]