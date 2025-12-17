#!/bin/bash
set -e

echo "Pulling latest changes..."
git pull

echo "Building backend application..."
mvn clean package -DskipTests

echo "Building frontend application..."
cd frontend

# Build the Next.js application
echo "Building Next.js application..."
npm ci
npm run build

cd ..

echo "Building and starting Docker containers..."
docker compose build --no-cache
docker compose up -d

echo "Deployment complete!"
