export interface Message {
  id: string;
  content: string;
  role: 'user' | 'assistant';
  timestamp: string | Date;
}

export interface ChatInput {
  chat: string;
}

export interface ChatOutput {
  chat: string;
}

export interface ChatSession {
  id: string;
  title: string;
  createdAt: string;
  updatedAt: string;
  messages?: Message[];
}

export interface CreateSessionRequest {
  title: string;
}

export interface SendMessageRequest {
  message: string;
}

