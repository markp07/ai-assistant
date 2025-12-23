# HTTP Test Scripts

This directory contains example scripts for testing the AI Assistant API.

## Scripts

### postChat.sh
Bash script demonstrating the session-based chat API.

**Prerequisites:**
- `jq` command-line JSON processor: `brew install jq` (macOS) or `apt-get install jq` (Linux)
- JWT token from http://localhost:7080

**Usage:**
```bash
# Edit the script and replace YOUR_JWT_TOKEN_HERE with your actual token
./http/postChat.sh
```

### chatbot.py
Interactive Python chatbot using the session-based chat API.

**Prerequisites:**
- Python 3.x
- requests library: `pip install requests`
- JWT token from http://localhost:7080

**Usage:**
```bash
python http/chatbot.py YOUR_JWT_TOKEN
```

**Features:**
- Creates a new chat session automatically
- Interactive chat interface
- Type `history` to see conversation history
- Type `exit` to quit

**Example Session:**
```
$ python http/chatbot.py eyJhbGc...
Creating new chat session...
Session created: abc-123-def

Type your messages (type 'exit' to quit, 'history' to see conversation):
> What can I do in Malaga?
Assistant: Here are some great things to do in Malaga...

> history
--- Conversation History ---
USER: What can I do in Malaga?
ASSISTANT: Here are some great things to do in Malaga...
--- End of History ---

> exit
Goodbye!
```

## Getting a JWT Token

1. Visit http://localhost:7080/login?callback=http://localhost:3000
2. Log in with your credentials
3. After successful login, you'll be redirected with tokens in the URL
4. Copy the `access_token` from the URL or browser storage
5. Use this token in the scripts above

## API Endpoints Used

These scripts use the new session-based endpoints:
- `POST /api/v1/sessions` - Create chat session
- `POST /api/v1/sessions/{sessionId}/messages` - Send message
- `GET /api/v1/sessions/{sessionId}/history` - Get history

For full API documentation, see `/API_MIGRATION_GUIDE.md` or the OpenAPI spec at `src/main/resources/api/ai-assistant-api.yml`.

