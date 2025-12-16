import api from './api';

export const ticketService = {
  // Crear ticket
  createTicket: async (ticketData) => {
    const response = await api.post('/support-tickets', ticketData);
    return response.data;
  },

  // Obtener tickets con filtros
  getTickets: async (filters = {}) => {
    const params = new URLSearchParams();
    
    if (filters.q) params.append('q', filters.q);
    if (filters.status) params.append('status', filters.status);
    if (filters.currency) params.append('currency', filters.currency);
    if (filters.minCost) params.append('minCost', filters.minCost);
    if (filters.maxCost) params.append('maxCost', filters.maxCost);
    if (filters.from) params.append('from', filters.from);
    if (filters.to) params.append('to', filters.to);
    
    params.append('page', filters.page || 0);
    params.append('size', filters.size || 10);
    params.append('sort', filters.sort || 'createdAt,desc');
    
    const response = await api.get(`/support-tickets?${params.toString()}`);
    return response.data;
  }
};