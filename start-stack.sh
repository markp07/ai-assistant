#!/usr/bin/env bash
docker system prune -f
docker build -t ai-assistant:latest .
docker stack deploy --compose-file docker-compose.yml ai-assistant
