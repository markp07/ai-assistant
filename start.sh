#!/usr/bin/env bash
docker system prune -f
docker stack deploy --compose-file docker-compose.yml ai-assistant