import { ChatSession, CreateSessionRequest, Message, SendMessageRequest } from '@/types/chat';
import { refreshAccessToken, redirectToLogin } from './auth';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:7075';

/**
 * Makes an authenticated request to the backend API with automatic token refresh on 401
 * @param url The URL to fetch
 * @param options Fetch options
 * @returns Response or throws error if authentication failed
 */
async function fetchWithAuth(url: string, options: RequestInit = {}): Promise<Response> {
  // First attempt with credentials (cookies)
  let response = await fetch(url, {
    ...options,
    credentials: 'include', // Include HTTP-only cookies
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
  });

  // If we get a 401, try to refresh the token and retry
  if (response.status === 401) {
    console.log('[API] Got 401, attempting token refresh...');
    const refreshed = await refreshAccessToken();

    if (!refreshed) {
      console.log('[API] Token refresh failed, redirecting to login');
      throw new Error('Authentication failed');
    }

    // Retry the original request with the new token
    console.log('[API] Token refreshed, retrying request...');
    response = await fetch(url, {
      ...options,
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    if (response.status === 401) {
      // Still unauthorized after refresh, redirect to login
      console.log('[API] Still unauthorized after refresh, redirecting to login');
      redirectToLogin();
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

export async function updateSession(sessionId: string, title: string): Promise<ChatSession> {
  const response = await fetchWithAuth(`${API_BASE_URL}/api/v1/sessions/${sessionId}`, {
    method: 'PUT',
    body: JSON.stringify({ title } as CreateSessionRequest),
  });

  if (!response.ok) {
    throw new Error(`Failed to update session: ${response.statusText}`);
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
