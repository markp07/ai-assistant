export const AUTH_URL = process.env.NEXT_PUBLIC_AUTH_URL || 'http://localhost:7080';

/**
 * Redirects the user to the login page with a callback to return to the current page
 */
export function redirectToLogin(): void {
  if (typeof window === 'undefined') return;
  const callback = encodeURIComponent(window.location.origin);
  window.location.href = `${AUTH_URL}/login?callback=${callback}`;
}

/**
 * Refreshes the access token using the refresh token stored in HTTP-only cookie
 * @returns true if refresh was successful, false otherwise
 */
export async function refreshAccessToken(): Promise<boolean> {
  try {
    const response = await fetch(`${AUTH_URL}/api/auth/v1/refresh`, {
      method: 'POST',
      credentials: 'include', // Include cookies in the request
    });

    if (!response.ok) {
      redirectToLogin();
      return false;
    }

    return true;
  } catch (error) {
    console.error('Error refreshing token:', error);
    redirectToLogin();
    return false;
  }
}

/**
 * Makes an authenticated request to the auth service with automatic token refresh on 401
 * @param url The URL to fetch
 * @param options Fetch options
 * @returns Response or null if authentication failed
 */
async function authenticatedFetch(url: string, options: RequestInit = {}): Promise<Response | null> {
  // First attempt with credentials (cookies)
  const response = await fetch(url, {
    ...options,
    credentials: 'include',
  });

  // If we get a 401, try to refresh the token and retry
  if (response.status === 401) {
    console.log('[Auth] Got 401, attempting token refresh...');
    const refreshed = await refreshAccessToken();

    if (!refreshed) {
      console.log('[Auth] Token refresh failed, redirecting to login');
      return null;
    }

    // Retry the original request with the new token
    console.log('[Auth] Token refreshed, retrying request...');
    const retryResponse = await fetch(url, {
      ...options,
      credentials: 'include',
    });

    if (retryResponse.status === 401) {
      // Still unauthorized after refresh, redirect to login
      console.log('[Auth] Still unauthorized after refresh, redirecting to login');
      redirectToLogin();
      return null;
    }

    return retryResponse;
  }

  return response;
}

export interface UserInfo {
  email: string;
  userName: string;
  twoFactorEnabled: boolean;
  passkeyEnabled: boolean;
  emailVerified: boolean;
  createdAt: string;
}

/**
 * Fetches the current user's information from the auth service
 * Automatically retries with token refresh if the first request returns 401
 * @returns UserInfo or null if request failed or user is not authenticated
 */
export async function fetchUserInfo(): Promise<UserInfo | null> {
  try {
    const response = await authenticatedFetch(`${AUTH_URL}/api/auth/v1/user`);

    if (!response || !response.ok) {
      return null;
    }

    const userInfo: UserInfo = await response.json();
    return userInfo;
  } catch (error) {
    console.error('Error fetching user info:', error);
    return null;
  }
}

/**
 * Logs out the current user by calling the auth service logout endpoint
 * Automatically retries with token refresh if the first request returns 401
 * Then redirects to the login page
 */
export async function logout(): Promise<void> {
  try {
    const response = await authenticatedFetch(`${AUTH_URL}/api/auth/v1/logout`, {
      method: 'POST',
    });

    if (!response) {
      console.log('[Auth] Logout request failed, user may already be logged out');
    }
  } catch (error) {
    console.error('Error during logout:', error);
    // Continue with redirect even if API call fails
  }

  // Always redirect to login after logout, cookies will be cleared by the auth service
  redirectToLogin();
}

