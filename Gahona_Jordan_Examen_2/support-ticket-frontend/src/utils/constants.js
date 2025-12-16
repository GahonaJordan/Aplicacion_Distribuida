export const TICKET_STATUS = {
  OPEN: 'OPEN',
  IN_PROGRESS: 'IN_PROGRESS',
  RESOLVED: 'RESOLVED',
  CLOSED: 'CLOSED',
  CANCELLED: 'CANCELLED'
};

export const PRIORITY = {
  LOW: 'LOW',
  MEDIUM: 'MEDIUM',
  HIGH: 'HIGH',
  CRITICAL: 'CRITICAL'
};

export const CURRENCY = {
  USD: 'USD',
  EUR: 'EUR'
};

export const CATEGORY = {
  NETWORK: 'NETWORK',
  HARDWARE: 'HARDWARE',
  SOFTWARE: 'SOFTWARE'
};

export const STATUS_COLORS = {
  OPEN: 'bg-gradient-to-r from-blue-500 to-cyan-500 text-white border-0 shadow-lg shadow-blue-200',
  IN_PROGRESS: 'bg-gradient-to-r from-amber-500 to-orange-500 text-white border-0 shadow-lg shadow-amber-200',
  RESOLVED: 'bg-gradient-to-r from-emerald-500 to-green-500 text-white border-0 shadow-lg shadow-emerald-200',
  CLOSED: 'bg-gradient-to-r from-slate-500 to-gray-500 text-white border-0 shadow-lg shadow-slate-200',
  CANCELLED: 'bg-gradient-to-r from-rose-500 to-red-500 text-white border-0 shadow-lg shadow-rose-200'
};

export const PRIORITY_COLORS = {
  LOW: 'bg-gradient-to-br from-slate-400 to-slate-500 text-white shadow-md',
  MEDIUM: 'bg-gradient-to-br from-blue-500 to-indigo-600 text-white shadow-md',
  HIGH: 'bg-gradient-to-br from-orange-500 to-red-500 text-white shadow-md',
  CRITICAL: 'bg-gradient-to-br from-red-600 to-rose-700 text-white shadow-lg animate-pulse'
};

export const STATUS_LABELS = {
  OPEN: 'Abierto',
  IN_PROGRESS: 'En Progreso',
  RESOLVED: 'Resuelto',
  CLOSED: 'Cerrado',
  CANCELLED: 'Cancelado'
};

export const PRIORITY_LABELS = {
  LOW: 'Baja',
  MEDIUM: 'Media',
  HIGH: 'Alta',
  CRITICAL: 'Crítica'
};

export const CATEGORY_LABELS = {
  NETWORK: 'Red',
  HARDWARE: 'Hardware',
  SOFTWARE: 'Software'
};