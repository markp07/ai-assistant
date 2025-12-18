'use client';

import { useState, useEffect } from 'react';
import { Chat } from '@/components/Chat';
import { Sidebar } from '@/components/Sidebar';
import { AuthProvider } from '@/components/AuthProvider';
import { createSession } from '@/lib/api';

function HomeContent() {
  const [currentSessionId, setCurrentSessionId] = useState<string | undefined>();

  useEffect(() => {
    // Create initial session
    initializeSession();
  }, []);

  const initializeSession = async () => {
    try {
      const session = await createSession('New Chat');
      setCurrentSessionId(session.id);
    } catch (error) {
      console.error('Error creating initial session:', error);
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
