#!/bin/bash

# Example script for testing the chat API with sessions
# First, get your JWT token from auth.markpost.dev
# Then set it in the TOKEN variable below

TOKEN="YOUR_JWT_TOKEN_HERE"
API_BASE_URL="http://localhost:7075/api/v1"

# Step 1: Create a new chat session
echo "Creating new chat session..."
SESSION_RESPONSE=$(curl -s -X POST "$API_BASE_URL/sessions" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{"title": "Test Chat"}')

echo "Session created: $SESSION_RESPONSE"

# Extract session ID (requires jq)
SESSION_ID=$(echo $SESSION_RESPONSE | jq -r '.id')
echo "Session ID: $SESSION_ID"

# Step 2: Send a message to the session
echo -e "\nSending message..."
curl -X POST "$API_BASE_URL/sessions/$SESSION_ID/messages" \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer $TOKEN" \
     -d '{"message": "What can I do in Malaga?"}'

echo -e "\n\nDone!"
