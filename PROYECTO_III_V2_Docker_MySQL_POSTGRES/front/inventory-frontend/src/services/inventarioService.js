import axios from 'axios';
import { INVENTARIO_API } from './api';

export const inventarioService = {
  // Obtener todo el inventario
  getAll: async () => {
    const response = await axios.get(`${INVENTARIO_API}/inventarios`);
    return response.data;
  },

  // Inventario por bodega
  getByBodega: async (bodegaId) => {
    const response = await axios.get(`${INVENTARIO_API}/inventarios/bodega/${bodegaId}`);
    return response.data;
  },

  // Inventario por producto
  getByProducto: async (productoId) => {
    const response = await axios.get(`${INVENTARIO_API}/inventarios/producto/${productoId}`);
    return response.data;
  },

  // Movimientos por bodega
  getMovimientosByBodega: async (bodegaId) => {
    const response = await axios.get(`${INVENTARIO_API}/inventarios/bodega/${bodegaId}/movimientos`);
    return response.data;
  },

  // Registrar movimiento individual
  registrarMovimiento: async (bodegaId, movimiento) => {
    const response = await axios.post(
      `${INVENTARIO_API}/inventarios/bodega/${bodegaId}/movimientos`,
      movimiento
    );
    return response.data;
  }
};