import requests
import json

API_URL = "http://localhost:7075/api/v1/chat"
HEADERS = {"Content-Type": "application/json"}

def send_chat(message):
    payload = {"chat": message}
    response = requests.post(API_URL, headers=HEADERS, data=json.dumps(payload))
    response_data = response.json()
    return response_data.get("chat", "No chat field in response")

def main():
    print("Type your message (type 'exit' to quit):")
    while True:
        user_input = input()
        if user_input.lower() == "exit":
            break
        try:
            response = send_chat(user_input)
            print("Response:", response)
        except Exception as e:
            print("Error:", e)

if __name__ == "__main__":
    main()