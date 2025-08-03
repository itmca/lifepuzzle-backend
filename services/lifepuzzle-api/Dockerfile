# Multi-stage build for Spring Boot application
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY shared/java-common ./shared/java-common

# Copy service source
COPY services/lifepuzzle-api ./services/lifepuzzle-api

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application (skip tests for faster build)
RUN ./gradlew services:lifepuzzle-api:bootJar -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Install curl for health checks
RUN apk --no-cache add curl

# Create non-root user
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copy the built jar
COPY --from=builder --chown=appuser:appgroup /app/services/lifepuzzle-api/build/libs/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]