import axios from 'axios';
import { authService } from './AuthService';

// En producción (Docker), Nginx hace proxy de /api/* a los microservicios
// En desarrollo (Vite), el proxy de vite.config.js redirige las llamadas
const PRODUCTO_API = '/api/productos';
const PROVEEDOR_API = '/api/proveedores';
const INVENTARIO_API = '/api';
const ORDEN_API = '/api/ordenes-compra';

// Configuración global de axios
axios.defaults.headers.common['Content-Type'] = 'application/json';

// Interceptor para agregar el token JWT a todas las peticiones
axios.interceptors.request.use(
  (config) => {
    const token = authService.getAccessToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores globalmente
axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // El servidor respondió con un código de error
      console.error('Error en respuesta del servidor:', {
        status: error.response.status,
        data: error.response.data,
        url: error.config.url,
        method: error.config.method,
        requestData: error.config.data
      });
      
      // Si es un error 401 (no autorizado), logout
      if (error.response.status === 401) {
        authService.logout();
        window.location.href = '/login';
      }
    } else if (error.request) {
      // La petición fue hecha pero no hubo respuesta
      console.error('No se recibió respuesta del servidor:', {
        url: error.config.url,
        method: error.config.method,
        message: 'Verifica que el servicio esté corriendo'
      });
    } else {
      // Algo pasó al configurar la petición
      console.error('Error en la petición:', error.message);
    }
    return Promise.reject(error);
  }
);

export { PRODUCTO_API, PROVEEDOR_API, INVENTARIO_API, ORDEN_API };