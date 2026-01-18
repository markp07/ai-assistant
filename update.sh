#!/bin/bash

# Pull updates only for redis and postgres
docker compose pull postgres

# Bring up the updated services (only redis and postgres)
docker compose up -d postgres

# Prune the system
docker system prune -f

