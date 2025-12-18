'use client';

import { useEffect, useState } from 'react';
import { fetchUserInfo, type UserInfo, clearTokens, redirectToLogin } from '@/lib/auth';

export function UserProfile() {
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadUserInfo = async () => {
      const info = await fetchUserInfo();
      setUserInfo(info);
      setIsLoading(false);
    };

    loadUserInfo();
  }, []);

  const handleLogout = () => {
    if (window.confirm('Are you sure you want to logout?')) {
      clearTokens();
      redirectToLogin();
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center gap-2">
        <div className="w-8 h-8 bg-gray-300 dark:bg-gray-600 rounded-full animate-pulse"></div>
      </div>
    );
  }

  if (!userInfo) {
    return null;
  }

  return (
    <div className="flex items-center gap-3">
      <div className="flex items-center gap-2">
        <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center text-white font-semibold">
          {userInfo.userName.charAt(0).toUpperCase()}
        </div>
        <span className="hidden sm:inline text-sm font-medium text-gray-700 dark:text-gray-300">
          {userInfo.userName}
        </span>
      </div>
      <button
        onClick={handleLogout}
        className="text-sm px-3 py-1.5 rounded-lg bg-gray-200 dark:bg-gray-700 text-gray-700 dark:text-gray-300 hover:bg-gray-300 dark:hover:bg-gray-600 transition-colors"
        title="Logout"
      >
        Logout
      </button>
    </div>
  );
}

