'use client';

import { useEffect, useState } from 'react';

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initAuth = () => {
      // Tokens are now stored as HTTP-only cookies by the auth service
      // No need to extract them from URL or store in localStorage
      // The browser will automatically send them with requests

      // Simply mark as ready - let the backend verify tokens via cookies
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

