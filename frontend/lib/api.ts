import { ChatSession, CreateSessionRequest, Message, SendMessageRequest } from '@/types/chat';
import { getTokens, refreshAccessToken, redirectToLogin } from './auth';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:7075';

async function fetchWithAuth(url: string, options: RequestInit = {}): Promise<Response> {
  const tokens = getTokens();

  if (!tokens) {
    redirectToLogin();
    throw new Error('Not authenticated');
  }

  const headers = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${tokens.access_token}`,
    ...options.headers,
  };

  let response = await fetch(url, { ...options, headers });

  // If we get a 401, try to refresh the token
  if (response.status === 401) {
    const refreshed = await refreshAccessToken();
    if (refreshed) {
      const newTokens = getTokens();
      if (newTokens) {
        headers.Authorization = `Bearer ${newTokens.access_token}`;
        response = await fetch(url, { ...options, headers });
      }
    } else {
      throw new Error('Authentication failed');
    }
  }

  return response;
}

export async function createSession(title?: string): Promise<ChatSession> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions`, {
    method: 'POST',
    body: JSON.stringify({ title: title || 'New Chat' } as CreateSessionRequest),
  });

  if (!response.ok) {
    throw new Error(`Failed to create session: ${response.statusText}`);
  }

  return response.json();
}

export async function getSessions(): Promise<ChatSession[]> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions`);

  if (!response.ok) {
    throw new Error(`Failed to get sessions: ${response.statusText}`);
  }

  return response.json();
}

export async function getSession(sessionId: string): Promise<ChatSession> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions/${sessionId}`);

  if (!response.ok) {
    throw new Error(`Failed to get session: ${response.statusText}`);
  }

  return response.json();
}

export async function getSessionHistory(sessionId: string): Promise<Message[]> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions/${sessionId}/history`);

  if (!response.ok) {
    throw new Error(`Failed to get session history: ${response.statusText}`);
  }

  return response.json();
}

export async function sendMessage(sessionId: string, message: string): Promise<Message> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions/${sessionId}/messages`, {
    method: 'POST',
    body: JSON.stringify({ message } as SendMessageRequest),
  });

  if (!response.ok) {
    throw new Error(`Failed to send message: ${response.statusText}`);
  }

  return response.json();
}

export async function deleteSession(sessionId: string): Promise<void> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions/${sessionId}`, {
    method: 'DELETE',
  });

  if (!response.ok) {
    throw new Error(`Failed to delete session: ${response.statusText}`);
  }
}
