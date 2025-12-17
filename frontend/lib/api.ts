import { ChatInput, ChatOutput } from '@/types/chat';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:7075';

export async function sendMessage(message: string): Promise<string> {
  const response = await fetch(`${API_BASE_URL}/api/chat`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ chat: message } as ChatInput),
  });

  if (!response.ok) {
    throw new Error(`Failed to send message: ${response.statusText}`);
  }

  const data: ChatOutput = await response.json();
  return data.chat;
}

export async function clearChatHistory(): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/chat`, {
    method: 'DELETE',
  });

  if (!response.ok) {
    throw new Error(`Failed to clear chat history: ${response.statusText}`);
  }
}
