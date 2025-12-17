#!/usr/bin/env bash
set -e

echo "Cleaning Docker system..."
docker system prune -f

echo "Building frontend application..."
cd frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install
fi

# Build the Next.js application
echo "Building Next.js application..."
npm run build

cd ..

echo "Building and starting Docker containers..."
docker compose build --no-cache
docker compose up -d

echo "Deployment complete!"