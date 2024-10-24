#!/usr/bin/env bash

# Ensure Docker Buildx is set up
docker buildx create --use

# Build the multi-platform Docker image with insecure registry configuration
docker buildx build --platform linux/amd64,linux/arm64/v8 \
  --tag markpost/ai-assistant:latest \
  --tag markpost/ai-assistant:1.0.0 \
  --push \
  --build-arg BUILDKIT_INLINE_CACHE=1 \
  --build-arg DOCKER_BUILDKIT=1 \
  --build-arg DOCKER_CLI_EXPERIMENTAL=enabled \
  .