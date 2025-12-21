import requests
import json
import sys

API_BASE_URL = "http://localhost:7075/api/v1"

# Get JWT token from command line argument
# Usage: python chatbot.py YOUR_JWT_TOKEN
if len(sys.argv) < 2:
    print("Usage: python chatbot.py YOUR_JWT_TOKEN")
    print("Get your token from https://auth.markpost.dev")
    sys.exit(1)

JWT_TOKEN = sys.argv[1]
HEADERS = {
    "Content-Type": "application/json",
    "Authorization": f"Bearer {JWT_TOKEN}"
}

def create_session(title="Python Chatbot Session"):
    """Create a new chat session"""
    payload = {"title": title}
    response = requests.post(f"{API_BASE_URL}/sessions", headers=HEADERS, data=json.dumps(payload))
    response.raise_for_status()
    return response.json()["id"]

def send_message(session_id, message):
    """Send a message to a chat session"""
    payload = {"message": message}
    response = requests.post(
        f"{API_BASE_URL}/sessions/{session_id}/messages",
        headers=HEADERS,
        data=json.dumps(payload)
    )
    response.raise_for_status()
    response_data = response.json()
    return response_data.get("content", "No content in response")

def get_session_history(session_id):
    """Get the message history for a session"""
    response = requests.get(f"{API_BASE_URL}/sessions/{session_id}/history", headers=HEADERS)
    response.raise_for_status()
    return response.json()

def main():
    try:
        # Create a new chat session
        print("Creating new chat session...")
        session_id = create_session()
        print(f"Session created: {session_id}\n")

        print("Type your messages (type 'exit' to quit, 'history' to see conversation):")
        while True:
            user_input = input("> ")

            if user_input.lower() == "exit":
                print("Goodbye!")
                break

            if user_input.lower() == "history":
                history = get_session_history(session_id)
                print("\n--- Conversation History ---")
                for msg in history:
                    role = msg.get("role", "unknown").upper()
                    content = msg.get("content", "")
                    print(f"{role}: {content}")
                print("--- End of History ---\n")
                continue

            try:
                response = send_message(session_id, user_input)
                print(f"Assistant: {response}\n")
            except Exception as e:
                print(f"Error sending message: {e}\n")

    except Exception as e:
        print(f"Error: {e}")
        sys.exit(1)

if __name__ == "__main__":
    main()

