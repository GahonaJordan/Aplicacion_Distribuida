// src/services/AuthService.js
const API_URL = 'http://localhost:9000';

export const authService = {
  register: async (username, email, password, confirmPassword, firstName, lastName) => {
    try {
      const response = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          email,
          password,
          confirmPassword,
          firstName,
          lastName,
        }),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error en el registro');
      }

      return await response.json();
    } catch (error) {
      console.error('Register error:', error);
      throw error;
    }
  },

  login: async (username, password) => {
    try {
      const response = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          password,
        }),
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error en el login');
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  },

  getOAuthToken: async (code) => {
    try {
      const response = await fetch(`${API_URL}/oauth2/token`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          grant_type: 'authorization_code',
          code,
          client_id: 'proyecto_final',
          client_secret: 'proyecto_final_secret',
          redirect_uri: 'http://localhost:3000/callback',
        }),
      });

      if (!response.ok) {
        throw new Error('Error obtaining token');
      }

      return await response.json();
    } catch (error) {
      console.error('OAuth token error:', error);
      throw error;
    }
  },

  logout: () => {
    localStorage.removeItem('user');
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  getAccessToken: () => {
    return localStorage.getItem('accessToken');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },
};
