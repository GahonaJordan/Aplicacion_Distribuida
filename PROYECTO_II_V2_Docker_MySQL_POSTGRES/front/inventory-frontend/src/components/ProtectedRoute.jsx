// src/components/ProtectedRoute.jsx
import React from 'react';
import { useAuth } from '../context/AuthContext';
import { LoginRegister } from './LoginRegister';
import { useNavigate } from 'react-router-dom';

export const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();
  const navigate = useNavigate();

  if (loading) {
    return (
      <div style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '100vh',
        background: '#f5f5f5'
      }}>
        <div style={{ textAlign: 'center' }}>
          <h2>Cargando...</h2>
          <p>Por favor espera</p>
        </div>
      </div>
    );
  }

  if (!isAuthenticated) {
    return <LoginRegister onSuccess={() => navigate('/')} />;
  }

  return children;
};
