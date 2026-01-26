import axios from 'axios';
import { PRODUCTO_API } from './api';

export const productoService = {
  // Obtener todos los productos
  getAll: async () => {
    const response = await axios.get(PRODUCTO_API);
    return response.data;
  },

  // Obtener producto por ID
  getById: async (id) => {
    const response = await axios.get(`${PRODUCTO_API}/${id}`);
    return response.data;
  },

  // Crear producto
  create: async (producto) => {
    const response = await axios.post(PRODUCTO_API, producto);
    return response.data;
  },

  // Actualizar producto
  update: async (id, producto) => {
    const response = await axios.put(`${PRODUCTO_API}/${id}`, producto);
    return response.data;
  },

  // Eliminar producto
  delete: async (id) => {
    await axios.delete(`${PRODUCTO_API}/${id}`);
  },

  // Obtener solo productos activos
  getActive: async () => {
    const response = await axios.get(`${PRODUCTO_API}?status=ACTIVE`);
    return response.data;
  },

  // Obtener productos por estado
  getByStatus: async (status) => {
    const response = await axios.get(`${PRODUCTO_API}?status=${status}`);
    return response.data;
  },

  // Obtener categorías
  getCategories: async () => {
    const response = await axios.get(`${PRODUCTO_API}/categorias`);
    return response.data;
  }
};