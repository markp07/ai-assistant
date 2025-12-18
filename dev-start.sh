#!/bin/bash

# Local Development Setup Script for AI Assistant
# This script sets up and runs the application for local development

set -e

echo "🚀 AI Assistant - Local Development Setup"
echo "=========================================="

# Check if .env file exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Creating from .env.example..."
    if [ -f .env.example ]; then
        cp .env.example .env
        echo "✅ Created .env file. Please edit it with your configuration."
        echo "   Required: OPENAI_API_KEY"
        exit 1
    else
        echo "❌ .env.example not found. Please create .env file manually."
        exit 1
    fi
fi

# Check if OPENAI_API_KEY is set
if ! grep -q "OPENAI_API_KEY=sk-" .env; then
    echo "⚠️  OPENAI_API_KEY not configured in .env file."
    echo "   Please add your OpenAI API key to .env file."
    exit 1
fi

# Check Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker first."
    exit 1
fi

if ! command -v docker compose &> /dev/null; then
    echo "❌ Docker Compose is not installed. Please install Docker Compose first."
    exit 1
fi

echo ""
echo "📦 Starting services..."
echo ""

# Start PostgreSQL only first
echo "1️⃣  Starting PostgreSQL database..."
docker compose up -d postgres

# Wait for PostgreSQL to be healthy
echo "⏳ Waiting for PostgreSQL to be ready..."
timeout=60
elapsed=0
while [ $elapsed -lt $timeout ]; do
    if docker compose exec -T postgres pg_isready -U postgres &> /dev/null; then
        echo "✅ PostgreSQL is ready!"
        break
    fi
    sleep 2
    elapsed=$((elapsed + 2))
    echo -n "."
done

if [ $elapsed -ge $timeout ]; then
    echo ""
    echo "❌ PostgreSQL failed to start within ${timeout} seconds"
    docker compose logs postgres
    exit 1
fi

echo ""
echo "2️⃣  Building and starting backend..."
docker compose up -d --build ai-assistant

echo ""
echo "⏳ Waiting for backend to be ready..."
timeout=90
elapsed=0
while [ $elapsed -lt $timeout ]; do
    if curl -sf http://localhost:12501/actuator/health &> /dev/null; then
        echo "✅ Backend is ready!"
        break
    fi
    sleep 3
    elapsed=$((elapsed + 3))
    echo -n "."
done

if [ $elapsed -ge $timeout ]; then
    echo ""
    echo "⚠️  Backend may still be starting. Check logs: docker compose logs ai-assistant"
fi

echo ""
echo "3️⃣  Building and starting frontend..."
docker compose up -d --build frontend

echo ""
echo "⏳ Waiting for frontend to be ready..."
timeout=60
elapsed=0
while [ $elapsed -lt $timeout ]; do
    if curl -sf http://localhost:12502 &> /dev/null; then
        echo "✅ Frontend is ready!"
        break
    fi
    sleep 2
    elapsed=$((elapsed + 2))
    echo -n "."
done

echo ""
echo "=========================================="
echo "✅ AI Assistant is running!"
echo ""
echo "🌐 Access points:"
echo "   Frontend:  http://localhost:12502"
echo "   Backend:   http://localhost:12501"
echo "   API Docs:  http://localhost:12501/swagger-ui.html"
echo "   Health:    http://localhost:12501/actuator/health"
echo ""
echo "📊 Useful commands:"
echo "   View logs:        docker compose logs -f"
echo "   View backend:     docker compose logs -f ai-assistant"
echo "   View frontend:    docker compose logs -f frontend"
echo "   View database:    docker compose logs -f postgres"
echo "   Stop all:         docker compose down"
echo "   Stop & cleanup:   docker compose down -v"
echo ""
echo "🔍 Database access:"
echo "   docker compose exec postgres psql -U postgres -d ai_assistant"
echo ""
echo "=========================================="

