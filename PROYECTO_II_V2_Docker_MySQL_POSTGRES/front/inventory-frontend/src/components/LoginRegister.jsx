// src/components/LoginRegister.jsx
import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './LoginRegister.css';

export const LoginRegister = ({ onSuccess }) => {
  const { login, register } = useAuth();
  const [isLogin, setIsLogin] = useState(true);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Estado para Login
  const [loginData, setLoginData] = useState({
    username: '',
    password: '',
  });

  // Estado para Registro
  const [registerData, setRegisterData] = useState({
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
  });

  const handleLoginChange = (e) => {
    const { name, value } = e.target;
    setLoginData((prev) => ({
      ...prev,
      [name]: value,
    }));
    setError('');
  };

  const handleRegisterChange = (e) => {
    const { name, value } = e.target;
    setRegisterData((prev) => ({
      ...prev,
      [name]: value,
    }));
    setError('');
  };

  const handleLoginSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      await login(loginData.username, loginData.password);
      setSuccess('¡Login exitoso!');
      setTimeout(() => {
        onSuccess?.();
      }, 500);
    } catch (err) {
      setError(err.message || 'Error en el login');
    } finally {
      setLoading(false);
    }
  };

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    // Validaciones básicas
    if (registerData.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres');
      setLoading(false);
      return;
    }

    if (registerData.password !== registerData.confirmPassword) {
      setError('Las contraseñas no coinciden');
      setLoading(false);
      return;
    }

    try {
      await register(
        registerData.username,
        registerData.email,
        registerData.password,
        registerData.confirmPassword,
        registerData.firstName,
        registerData.lastName
      );
      setSuccess('¡Registro exitoso! Ahora inicia sesión.');
      setRegisterData({
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
      });
      setTimeout(() => {
        setIsLogin(true);
        setSuccess('');
      }, 1500);
    } catch (err) {
      setError(err.message || 'Error en el registro');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="auth-header">
          <h1>Sistema de Inventario</h1>
          <p>Gestiona tu inventario de manera eficiente</p>
        </div>

        <div className="auth-tabs">
          <button
            className={`tab-button ${isLogin ? 'active' : ''}`}
            onClick={() => {
              setIsLogin(true);
              setError('');
              setSuccess('');
            }}
          >
            Iniciar Sesión
          </button>
          <button
            className={`tab-button ${!isLogin ? 'active' : ''}`}
            onClick={() => {
              setIsLogin(false);
              setError('');
              setSuccess('');
            }}
          >
            Registrarse
          </button>
        </div>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        {isLogin ? (
          <form onSubmit={handleLoginSubmit} className="auth-form">
            <div className="form-group">
              <label htmlFor="username">Usuario</label>
              <input
                type="text"
                id="username"
                name="username"
                value={loginData.username}
                onChange={handleLoginChange}
                placeholder="Ingresa tu usuario"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Contraseña</label>
              <input
                type="password"
                id="password"
                name="password"
                value={loginData.password}
                onChange={handleLoginChange}
                placeholder="Ingresa tu contraseña"
                required
              />
            </div>

            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Iniciando sesión...' : 'Iniciar Sesión'}
            </button>
          </form>
        ) : (
          <form onSubmit={handleRegisterSubmit} className="auth-form">
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="firstName">Nombre</label>
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  value={registerData.firstName}
                  onChange={handleRegisterChange}
                  placeholder="Tu nombre"
                />
              </div>
              <div className="form-group">
                <label htmlFor="lastName">Apellido</label>
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  value={registerData.lastName}
                  onChange={handleRegisterChange}
                  placeholder="Tu apellido"
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="reg-username">Usuario</label>
              <input
                type="text"
                id="reg-username"
                name="username"
                value={registerData.username}
                onChange={handleRegisterChange}
                placeholder="Elige un usuario"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={registerData.email}
                onChange={handleRegisterChange}
                placeholder="tu@email.com"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="reg-password">Contraseña</label>
              <input
                type="password"
                id="reg-password"
                name="password"
                value={registerData.password}
                onChange={handleRegisterChange}
                placeholder="Mínimo 6 caracteres"
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="confirmPassword">Confirmar Contraseña</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={registerData.confirmPassword}
                onChange={handleRegisterChange}
                placeholder="Repite tu contraseña"
                required
              />
            </div>

            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Registrando...' : 'Registrarse'}
            </button>
          </form>
        )}

        <div className="auth-footer">
          <p>
            {isLogin
              ? '¿No tienes cuenta? Regístrate en la pestaña de arriba'
              : '¿Ya tienes cuenta? Inicia sesión en la pestaña de arriba'}
          </p>
        </div>
      </div>
    </div>
  );
};
