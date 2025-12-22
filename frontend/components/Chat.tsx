'use client';

import { useState, useRef, useEffect } from 'react';
import { Message } from '@/types/chat';
import { sendMessageStream, getSessionHistory } from '@/lib/api';
import { ChatMessage } from './ChatMessage';
import { ThemeToggle } from './ThemeToggle';
import { UserProfile } from './UserProfile';

interface ChatProps {
  sessionId?: string;
  sessionTitle?: string;
  onToggleSidebar: () => void;
}

export function Chat({ sessionId, sessionTitle, onToggleSidebar }: ChatProps) {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  useEffect(() => {
    if (sessionId) {
      loadHistory();
    } else {
      setMessages([]);
    }
  }, [sessionId]);

  const loadHistory = async () => {
    if (!sessionId) return;

    try {
      const history = await getSessionHistory(sessionId);
      setMessages(history);
    } catch (err) {
      console.error('Error loading history:', err);
      setError('Failed to load chat history');
    }
  };

  const handleSendMessage = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!input.trim() || isLoading || !sessionId) {
      return;
    }

    const userMessage: Message = {
      id: crypto.randomUUID(),
      content: input,
      role: 'user',
      timestamp: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, userMessage]);
    const messageContent = input;
    setInput('');
    setIsLoading(true);
    setError(null);

    // Create a placeholder message for the assistant's streaming response
    const assistantMessageId = crypto.randomUUID();
    const assistantMessage: Message = {
      id: assistantMessageId,
      content: '',
      role: 'assistant',
      timestamp: new Date().toISOString(),
    };

    setMessages((prev) => [...prev, assistantMessage]);

    try {
      await sendMessageStream(
        sessionId,
        messageContent,
        // onToken callback - update the assistant message as tokens arrive
        (token: string) => {
          setMessages((prev) =>
            prev.map((msg) => {
              if (msg.id === assistantMessageId) {
                // Add space before token if content already exists (not first token)
                const separator = msg.content === '' ? '' : ' ';
                return { ...msg, content: msg.content + separator + token };
              }
              return msg;
            })
          );
        },
        // onComplete callback
        () => {
          setIsLoading(false);
        },
        // onError callback
        (err: Error) => {
          setError(err.message || 'Failed to send message');
          console.error('Error sending message:', err);
          setIsLoading(false);
          // Remove the placeholder message on error
          setMessages((prev) => prev.filter((msg) => msg.id !== assistantMessageId));
        }
      );
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to send message');
      console.error('Error sending message:', err);
      setIsLoading(false);
      // Remove the placeholder message on error
      setMessages((prev) => prev.filter((msg) => msg.id !== assistantMessageId));
    }
  };

  return (
    <div className="flex flex-col full-height bg-gray-50 dark:bg-gray-900">
      {/* Header */}
      <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-4 py-3 sm:px-6">
        <div className="flex items-center justify-between">
          <h1 className="text-xl sm:text-2xl font-bold text-gray-900 dark:text-white truncate">
            {sessionTitle || 'AI Assistant'}
          </h1>
          <div className="flex items-center gap-2">
<button
  onClick={onToggleSidebar}
  className="p-2 sm:hidden hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition-colors"
  title="Toggle Sidebar"
>
  <svg className="w-6 h-6 text-gray-700 dark:text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
  </svg>
</button>
            <UserProfile />
          </div>
        </div>
      </header>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto mobile-scroll px-4 py-6 sm:px-6">
        {messages.length === 0 ? (
          <div className="flex flex-col items-center justify-center h-full text-center">
            <div className="mb-4">
              <svg
                className="w-16 h-16 text-gray-400 dark:text-gray-600 mx-auto"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M8 10h.01M12 10h.01M16 10h.01M9 16H5a2 2 0 01-2-2V6a2 2 0 012-2h14a2 2 0 012 2v8a2 2 0 01-2 2h-5l-5 5v-5z"
                />
              </svg>
            </div>
            <h2 className="text-xl font-semibold text-gray-700 dark:text-gray-300 mb-2">
              Start a conversation
            </h2>
            <p className="text-gray-500 dark:text-gray-400 max-w-md">
              Send a message to begin chatting with the AI assistant
            </p>
          </div>
        ) : (
          <>
            {messages.map((message) => (
              <ChatMessage key={message.id} message={message} />
            ))}
            {isLoading && (
              <div className="flex justify-start mb-4">
                <div className="bg-gray-200 dark:bg-gray-700 rounded-lg px-4 py-3">
                  <div className="flex space-x-2">
                    <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce" style={{ animationDelay: '0ms' }}></div>
                    <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce" style={{ animationDelay: '150ms' }}></div>
                    <div className="w-2 h-2 bg-gray-500 rounded-full animate-bounce" style={{ animationDelay: '300ms' }}></div>
                  </div>
                </div>
              </div>
            )}
            <div ref={messagesEndRef} />
          </>
        )}
      </div>

      {/* Error Display */}
      {error && (
        <div className="px-4 sm:px-6 py-2 bg-red-50 dark:bg-red-900/20 border-t border-red-200 dark:border-red-800">
          <p className="text-sm text-red-600 dark:text-red-400">{error}</p>
        </div>
      )}

      {/* Input Form */}
      <form
        onSubmit={handleSendMessage}
        className="bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 px-4 py-4 sm:px-6"
      >
        <div className="flex gap-2 sm:gap-3">
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Type your message..."
            disabled={isLoading}
            className="flex-1 rounded-lg border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 px-4 py-2 text-gray-900 dark:text-white placeholder-gray-500 dark:placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed"
          />
          <button
            type="submit"
            disabled={!input.trim() || isLoading}
            className="rounded-lg bg-blue-500 px-4 sm:px-6 py-2 text-white hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            Send
          </button>
        </div>
      </form>
    </div>
  );
}
