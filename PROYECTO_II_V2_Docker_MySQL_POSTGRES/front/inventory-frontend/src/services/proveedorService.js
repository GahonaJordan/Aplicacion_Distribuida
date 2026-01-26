import axios from 'axios';
import { PROVEEDOR_API } from './api';

export const proveedorService = {
  getAll: async () => {
    const response = await axios.get(PROVEEDOR_API);
    return response.data;
  },

  getById: async (id) => {
    const response = await axios.get(`${PROVEEDOR_API}/${id}`);
    return response.data;
  },

  create: async (proveedor) => {
    const response = await axios.post(PROVEEDOR_API, proveedor);
    return response.data;
  },

  update: async (id, proveedor) => {
    const response = await axios.put(`${PROVEEDOR_API}/${id}`, proveedor);
    return response.data;
  },

  delete: async (id) => {
    await axios.delete(`${PROVEEDOR_API}/${id}`);
  },

  // Obtener productos por estado
  getByStatus: async (status) => {
    const response = await axios.get(`${PROVEEDOR_API}?status=${status}`);
    return response.data;
  },
};