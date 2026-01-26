import axios from 'axios';
import { INVENTARIO_API } from './api';

export const bodegaService = {
  getAll: async () => {
    const response = await axios.get(`${INVENTARIO_API}/bodegas`);
    return response.data;
  },

  getById: async (id) => {
    const response = await axios.get(`${INVENTARIO_API}/bodegas/${id}`);
    return response.data;
  },

  create: async (bodega) => {
    console.log('Enviando bodega a crear:', bodega);
    const response = await axios.post(`${INVENTARIO_API}/bodegas`, bodega);
    return response.data;
  },

  update: async (id, bodega) => {
    console.log('Enviando bodega a actualizar:', { id, bodega });
    const response = await axios.put(`${INVENTARIO_API}/bodegas/${id}`, bodega);
    return response.data;
  },

  delete: async (id) => {
    await axios.delete(`${INVENTARIO_API}/bodegas/${id}`);
  },

  getActive: async () => {
    const response = await axios.get(`${INVENTARIO_API}/bodegas?status=ACTIVE`);
    return response.data;
  },

  getByStatus: async (status) => {
    const response = await axios.get(`${INVENTARIO_API}/bodegas?status=${status}`);
    return response.data;
  }
};