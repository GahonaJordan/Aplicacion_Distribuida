// src/services/userService.js
import axios from 'axios';
import { authService } from './AuthService';

const API_URL = 'http://localhost:9000';

export const userService = {
  getAll: async () => {
    try {
      const token = authService.getAccessToken();
      const response = await axios.get(`${API_URL}/api/users`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data.data;
    } catch (error) {
      console.error('Error al obtener usuarios:', error);
      throw error;
    }
  },

  getById: async (userId) => {
    try {
      const token = authService.getAccessToken();
      const response = await axios.get(`${API_URL}/api/users/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data.data;
    } catch (error) {
      console.error('Error al obtener usuario:', error);
      throw error;
    }
  },

  updateRoles: async (userId, roles) => {
    try {
      const token = authService.getAccessToken();
      const response = await axios.put(`${API_URL}/api/users/${userId}/roles`, {
        roles: roles
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error al actualizar roles:', error);
      throw error;
    }
  },

  delete: async (userId) => {
    try {
      const token = authService.getAccessToken();
      const response = await axios.delete(`${API_URL}/api/users/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error al eliminar usuario:', error);
      throw error;
    }
  }
};
