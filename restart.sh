#!/usr/bin/env bash
set -e

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    echo "Loading environment variables from .env file..."
    export $(cat .env | grep -v '^#' | grep -v '^$' | xargs)
else
    echo "Warning: .env file not found. Using default values."
fi

echo "Stopping services..."
docker compose down --volumes

echo "Starting services..."
docker compose up -d

echo "Services restarted!"
