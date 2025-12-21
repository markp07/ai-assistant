export interface AuthTokens {
  access_token: string;
  refresh_token: string;
}

export const AUTH_URL = process.env.NEXT_PUBLIC_AUTH_URL || 'https://auth.markpost.dev';

export function getTokens(): AuthTokens | null {
  if (typeof window === 'undefined') return null;

  const accessToken = localStorage.getItem('access_token');
  const refreshToken = localStorage.getItem('refresh_token');

  if (!accessToken || !refreshToken) return null;

  return {
    access_token: accessToken,
    refresh_token: refreshToken,
  };
}

export function setTokens(tokens: AuthTokens): void {
  localStorage.setItem('access_token', tokens.access_token);
  localStorage.setItem('refresh_token', tokens.refresh_token);
}

export function clearTokens(): void {
  localStorage.removeItem('access_token');
  localStorage.removeItem('refresh_token');
}

export function redirectToLogin(): void {

  const callback = encodeURIComponent(window.location.origin);
  window.location.href = `${AUTH_URL}/login?callback=${callback}`;
}

export async function refreshAccessToken(): Promise<boolean> {
  const tokens = getTokens();
  if (!tokens?.refresh_token) {
    clearTokens();
    redirectToLogin();
    return false;
  }

  try {
    const response = await fetch(`${AUTH_URL}/api/auth/v1/refresh`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ refresh_token: tokens.refresh_token }),
    });

    if (!response.ok) {
      clearTokens();
      redirectToLogin();
      return false;
    }

    const newTokens: AuthTokens = await response.json();
    setTokens(newTokens);
    return true;
  } catch (error) {
    console.error('Error refreshing token:', error);
    clearTokens();
    redirectToLogin();
    return false;
  }
}

export interface UserInfo {
  email: string;
  userName: string;
  twoFactorEnabled: boolean;
  passkeyEnabled: boolean;
  emailVerified: boolean;
  createdAt: string;
}

export async function fetchUserInfo(): Promise<UserInfo | null> {
  const tokens = getTokens();
  if (!tokens) {
    return null;
  }

  try {
    const response = await fetch(`${AUTH_URL}/api/auth/v1/user`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${tokens.access_token}`,
      },
    });

    if (!response.ok) {
      return null;
    }

    const userInfo: UserInfo = await response.json();
    return userInfo;
  } catch (error) {
    console.error('Error fetching user info:', error);
    return null;
  }
}

export async function logout(): Promise<void> {
  const tokens = getTokens();

  if (tokens?.access_token) {
    try {
      await fetch(`${AUTH_URL}/api/auth/v1/logout`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${tokens.access_token}`,
        },
      });
    } catch (error) {
      console.error('Error during logout:', error);
      // Continue with local cleanup even if API call fails
    }
  }

  clearTokens();
  redirectToLogin();
}

