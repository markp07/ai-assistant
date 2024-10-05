#!/bin/sh

# Build the Docker image
docker build -t ai-assistant:latest .

# Extract the version from the Docker image
VERSION=$(docker run --rm ai-assistant:latest cat /workspace/app/version.txt)

# Tag the Docker image with the extracted version
docker tag ai-assistant:latest ai-assistant:$VERSION

echo "Docker image tagged with version: $VERSION"