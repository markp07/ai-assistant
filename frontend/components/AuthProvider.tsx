'use client';

import { useEffect, useState } from 'react';
import { getTokens, setTokens, redirectToLogin } from '@/lib/auth';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initAuth = () => {
      // Check if we have tokens in URL (callback from auth service)
      const urlParams = new URLSearchParams(window.location.search);
      const accessToken = urlParams.get('access_token');
      const refreshToken = urlParams.get('refresh_token');

      if (accessToken && refreshToken) {
        setTokens({ access_token: accessToken, refresh_token: refreshToken });
        // Clean up URL
        window.history.replaceState({}, document.title, window.location.pathname);
      }

      // Always mark as authenticated - let the backend verify tokens
      // If tokens are invalid, backend will return 401 and trigger login
      setIsAuthenticated(true);
      setIsLoading(false);
    };

    initAuth();
  }, []);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50 dark:bg-gray-900">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
          <p className="text-gray-600 dark:text-gray-400">Loading...</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return null;
  }

  return <>{children}</>;
}

