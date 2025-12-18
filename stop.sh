#!/usr/bin/env bash
set -e

echo "Stopping all services and removing volumes..."
docker compose down --volumes

echo "Services stopped!"
