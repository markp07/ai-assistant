#!/bin/bash
set -e

echo "Pulling latest changes..."
git pull

echo "Building frontend application..."
cd frontend

# Build the Next.js application with environment variable
echo "Building Next.js application..."
npm ci

# Export the environment variable for Next.js build
export NEXT_PUBLIC_API_URL=${NEXT_PUBLIC_API_URL:-http://localhost:7075}
echo "Using API URL: $NEXT_PUBLIC_API_URL"

npm run build

cd ..

echo "Building and starting Docker containers..."
docker compose build --no-cache
docker compose up -d

echo "Deployment complete!"
