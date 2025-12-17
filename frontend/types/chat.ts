export interface Message {
  id: string;
  content: string;
  role: 'user' | 'assistant';
  timestamp: Date;
}

export interface ChatInput {
  chat: string;
}

export interface ChatOutput {
  chat: string;
}
