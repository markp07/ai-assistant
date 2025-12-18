#!/bin/bash
set -e

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)
else
    echo "Warning: .env file not found. Using default values."
fi

echo "Pulling latest changes..."
git pull


echo "Building and starting Docker containers..."
docker compose build --no-cache
docker compose up -d

echo "Deployment complete!"
