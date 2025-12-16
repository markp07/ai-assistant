#!/bin/bash
git pull

mvn clean package -DskipTests
docker compose build --no-cache
docker compose up -d
