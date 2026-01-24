# Multi-stage build for Spring Boot application with Python support
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradle ./gradle
COPY gradlew build.gradle settings.gradle ./
COPY shared/java-common ./shared/java-common

# Copy service source
COPY services/ai-video-generator ./services/ai-video-generator

# Make gradlew executable
RUN chmod +x ./gradlew

# Build the application (skip tests for faster build)
RUN ./gradlew services:ai-video-generator:bootJar -x test --no-daemon

# Runtime stage with Python support
FROM eclipse-temurin:21-jre

WORKDIR /app

# Update package list first
RUN apt-get update

# Install basic packages
RUN apt-get install -y --no-install-recommends \
    curl \
    ca-certificates \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install Python and development tools
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    python3 \
    python3-pip \
    python3-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Install multimedia and graphics libraries
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    ffmpeg \
    libgl1-mesa-glx \
    libglib2.0-0 \
    libsm6 \
    libxext6 \
    libxrender-dev \
    libgomp1 \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Create symbolic link for python command
RUN ln -s /usr/bin/python3 /usr/bin/python

# Declare runtime arguments for environment variables
ARG DATABASE_URL
ARG DATABASE_USERNAME
ARG DATABASE_PASSWORD
ARG RABBITMQ_HOST
ARG RABBITMQ_PORT
ARG RABBITMQ_ADMIN_PORT
ARG RABBITMQ_USERNAME
ARG RABBITMQ_PASSWORD
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ARG AWS_S3_BUCKET
ARG AWS_REGION
ARG AI_VIDEO_OUTPUT_PATH

# Set runtime environment variables
ENV DATABASE_URL=$DATABASE_URL
ENV DATABASE_USERNAME=$DATABASE_USERNAME
ENV DATABASE_PASSWORD=$DATABASE_PASSWORD
ENV RABBITMQ_HOST=$RABBITMQ_HOST
ENV RABBITMQ_PORT=$RABBITMQ_PORT
ENV RABBITMQ_ADMIN_PORT=$RABBITMQ_ADMIN_PORT
ENV RABBITMQ_USERNAME=$RABBITMQ_USERNAME
ENV RABBITMQ_PASSWORD=$RABBITMQ_PASSWORD
ENV AWS_ACCESS_KEY=$AWS_ACCESS_KEY
ENV AWS_SECRET_KEY=$AWS_SECRET_KEY
ENV AWS_S3_BUCKET=$AWS_S3_BUCKET
ENV AWS_REGION=$AWS_REGION
ENV AI_VIDEO_OUTPUT_PATH=$AI_VIDEO_OUTPUT_PATH
ENV PYTORCH_ENABLE_MPS_FALLBACK=1

# Create non-root user
RUN groupadd -g 1001 appgroup && \
    useradd -u 1001 -g appgroup -s /bin/bash -m appuser

# Create directories for AI video processing
RUN mkdir -p /var/tmp/ai_video && \
    chown -R appuser:appgroup /var/tmp/ai_video

# Copy the built jar
COPY --from=builder --chown=appuser:appgroup /app/services/ai-video-generator/build/libs/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8081

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]