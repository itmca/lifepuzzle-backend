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

# Declare build arguments for environment variables
ARG DB_PROD_URL
ARG DB_PROD_USERNAME
ARG DB_PROD_PASSWORD
ARG MAIL_HOST
ARG MAIL_PORT
ARG MAIL_USERNAME
ARG MAIL_PASSWORD
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ARG AWS_S3_BUCKET
ARG AWS_REGION
ARG DEEPGRAM_ACCESS_KEY
ARG SENTRY_DSN
ARG SLACK_WEBHOOK_URL
ARG OPENAI_API_URI
ARG OPENAI_API_KEY
ARG DEEPGRAM_API_URI
ARG DEEPGRAM_API_KEY
ARG APPLE_TEAM_ID
ARG APPLE_BUNDLE_ID
ARG APPLE_PRIVATE_KEY_ID
ARG APPLE_PRIVATE_KEY

# Set environment variables for build
ENV DB_PROD_URL=$DB_PROD_URL
ENV DB_PROD_USERNAME=$DB_PROD_USERNAME
ENV DB_PROD_PASSWORD=$DB_PROD_PASSWORD
ENV MAIL_HOST=$MAIL_HOST
ENV MAIL_PORT=$MAIL_PORT
ENV MAIL_USERNAME=$MAIL_USERNAME
ENV MAIL_PASSWORD=$MAIL_PASSWORD
ENV AWS_ACCESS_KEY=$AWS_ACCESS_KEY
ENV AWS_SECRET_KEY=$AWS_SECRET_KEY
ENV AWS_S3_BUCKET=$AWS_S3_BUCKET
ENV AWS_REGION=$AWS_REGION
ENV DEEPGRAM_ACCESS_KEY=$DEEPGRAM_ACCESS_KEY
ENV SENTRY_DSN=$SENTRY_DSN
ENV SLACK_WEBHOOK_URL=$SLACK_WEBHOOK_URL
ENV OPENAI_API_URI=$OPENAI_API_URI
ENV OPENAI_API_KEY=$OPENAI_API_KEY
ENV DEEPGRAM_API_URI=$DEEPGRAM_API_URI
ENV DEEPGRAM_API_KEY=$DEEPGRAM_API_KEY
ENV APPLE_TEAM_ID=$APPLE_TEAM_ID
ENV APPLE_BUNDLE_ID=$APPLE_BUNDLE_ID
ENV APPLE_PRIVATE_KEY_ID=$APPLE_PRIVATE_KEY_ID
ENV APPLE_PRIVATE_KEY=$APPLE_PRIVATE_KEY

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