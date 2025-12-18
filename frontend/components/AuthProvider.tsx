'use client';
}
  return <>{children}</>;

  }
    return null;
  if (!isAuthenticated) {

  }
    );
      </div>
        </div>
          <p className="text-gray-600 dark:text-gray-400">Loading...</p>
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600 mx-auto mb-4"></div>
        <div className="text-center">
      <div className="flex items-center justify-center h-screen bg-gray-50 dark:bg-gray-900">
    return (
  if (isLoading) {

  }, []);
    }
      redirectToLogin();
    } else {
      setIsLoading(false);
      setIsAuthenticated(true);
    if (tokens) {
    const tokens = getTokens();
    // Check if we have tokens in localStorage

    }
      return;
      setIsLoading(false);
      setIsAuthenticated(true);
      window.history.replaceState({}, document.title, window.location.pathname);
      // Clean up URL
      setTokens({ access_token: accessToken, refresh_token: refreshToken });
    if (accessToken && refreshToken) {

    const refreshToken = urlParams.get('refresh_token');
    const accessToken = urlParams.get('access_token');
    const urlParams = new URLSearchParams(window.location.search);
    // Check if we have tokens in URL (callback from auth service)
  useEffect(() => {

  const [isLoading, setIsLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
export function AuthProvider({ children }: { children: React.ReactNode }) {

import { getTokens, setTokens, redirectToLogin } from '@/lib/auth';
import { useEffect, useState } from 'react';


