# Multi-stage build for Spring Boot application
FROM eclipse-temurin:21-jdk AS builder

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
FROM eclipse-temurin:21-jre

WORKDIR /app

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -g 1001 appgroup && \
    useradd -u 1001 -g appgroup -s /bin/bash -m appuser

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