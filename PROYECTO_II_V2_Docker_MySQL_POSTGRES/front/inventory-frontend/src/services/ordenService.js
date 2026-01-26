import axios from 'axios';
import { ORDEN_API } from './api';

export const ordenService = {
  getAll: async () => {
    const response = await axios.get(ORDEN_API);
    return response.data;
  },

  getById: async (id) => {
    const response = await axios.get(`${ORDEN_API}/${id}`);
    return response.data;
  },

  getByEstado: async (estado) => {
    const response = await axios.get(`${ORDEN_API}/estado/${estado}`);
    return response.data;
  },

  create: async (orden) => {
    const response = await axios.post(ORDEN_API, orden);
    return response.data;
  },

  pendiente: async (id, notas) => {
    const response = await axios.patch(`${ORDEN_API}/${id}/pendiente`, {
      nuevoEstado: 'PENDIENTE',
        notas
    });
    return response.data;
  },

  aprobar: async (id, notas) => {
    const response = await axios.patch(`${ORDEN_API}/${id}/aprobar`, {
      nuevoEstado: 'APROBADA',
      notas
    });
    return response.data;
  },

  recibir: async (id, notas) => {
    const response = await axios.patch(`${ORDEN_API}/${id}/recibir`, {
      nuevoEstado: 'RECIBIDA',
      notas
    });
    return response.data;
  },

  cancelar: async (id, notas) => {
    const response = await axios.patch(`${ORDEN_API}/${id}/cancelar`, {
      nuevoEstado: 'CANCELADA',
      notas
    });
    return response.data;
  }
};