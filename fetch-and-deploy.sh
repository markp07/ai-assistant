#!/bin/bash

# Fetch the latest changes from the master branch
git fetch origin master

# Check if there are new changes
if git diff --quiet HEAD origin/master; then
  echo "No new changes."
else
  echo "New changes detected. Pulling changes and restarting the application."
  git pull origin master
  docker compose down --volumes
  docker system prune -f
  docker compose build --no-cache
  docker compose up -d
fi
