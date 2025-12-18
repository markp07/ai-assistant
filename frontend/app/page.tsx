'use client';

import { useState, useEffect } from 'react';
import { Chat } from '@/components/Chat';
import { Sidebar } from '@/components/Sidebar';
import { AuthProvider } from '@/components/AuthProvider';
import { createSession, getSessions } from '@/lib/api';

function HomeContent() {
  const [currentSessionId, setCurrentSessionId] = useState<string | undefined>();
  const [isInitializing, setIsInitializing] = useState(true);

  useEffect(() => {
    initializeSession();
  }, []);

  const initializeSession = async () => {
    try {
      // Try to get existing sessions first
      const sessions = await getSessions();

      if (sessions && sessions.length > 0) {
        // Open the newest session (first one, since they're sorted by updatedAt desc)
        setCurrentSessionId(sessions[0].id);
      } else {
        // No sessions exist, create a new one
        const session = await createSession('New Chat');
        setCurrentSessionId(session.id);
      }
    } catch (error) {
      console.error('Error initializing session:', error);
    } finally {
      setIsInitializing(false);
    }
  };

  const handleNewChat = async () => {
    try {
      const session = await createSession('New Chat');
      setCurrentSessionId(session.id);
    } catch (error) {
      console.error('Error creating session:', error);
    }
  };

  return (
    <div className="flex h-screen overflow-hidden">
      <Sidebar
        currentSessionId={currentSessionId}
        onSessionSelect={setCurrentSessionId}
        onNewChat={handleNewChat}
      />
      <div className="flex-1 overflow-hidden">
        <Chat sessionId={currentSessionId} />
      </div>
    </div>
  );
}

export default function Home() {
  return (
    <AuthProvider>
      <HomeContent />
    </AuthProvider>
  );
}
