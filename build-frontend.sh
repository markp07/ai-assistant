#!/bin/bash

# Build script for the frontend before Docker deployment

set -e

echo "Building frontend application..."
cd frontend

# Install dependencies if node_modules doesn't exist
if [ ! -d "node_modules" ]; then
    echo "Installing dependencies..."
    npm install
fi

# Build the Next.js application
echo "Building Next.js application..."
npm run build

echo "Frontend build complete!"
