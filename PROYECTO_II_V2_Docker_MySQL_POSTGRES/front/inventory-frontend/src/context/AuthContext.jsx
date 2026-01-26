// src/context/AuthContext.jsx
import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/AuthService';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar si hay usuario guardado al cargar
    const savedUser = authService.getCurrentUser();
    const token = authService.getAccessToken();
    
    if (savedUser && token) {
      setUser(savedUser);
      setIsAuthenticated(true);
    }
    setLoading(false);
  }, []);

  const register = async (username, email, password, confirmPassword, firstName, lastName) => {
    try {
      const response = await authService.register(
        username,
        email,
        password,
        confirmPassword,
        firstName,
        lastName
      );
      
      if (response.success) {
        // Después de registrar, hacer login automático
        return response;
      }
    } catch (error) {
      throw error;
    }
  };

  const login = async (username, password) => {
    try {
      const response = await authService.login(username, password);
      
      if (response.success) {
        setUser(response.data);
        localStorage.setItem('user', JSON.stringify(response.data));
        
        // Guardar el JWT token del servidor
        if (response.data && response.data.token) {
          localStorage.setItem('accessToken', response.data.token);
          localStorage.setItem('tokenType', response.data.tokenType || 'Bearer');
        }
        
        setIsAuthenticated(true);
        return response;
      }
    } catch (error) {
      throw error;
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
    setIsAuthenticated(false);
  };

  const value = {
    user,
    isAuthenticated,
    loading,
    register,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de AuthProvider');
  }
  return context;
};
