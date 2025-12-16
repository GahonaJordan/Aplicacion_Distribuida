import React, { useState } from 'react';
import { Search, Filter, X } from 'lucide-react';
import { TICKET_STATUS, CURRENCY, STATUS_LABELS } from '../utils/constants';

const FilterBar = ({ onFilterChange, initialFilters = {} }) => {
  const [filters, setFilters] = useState({
    q: initialFilters.q || '',
    status: initialFilters.status || '',
    currency: initialFilters.currency || '',
    minCost: initialFilters.minCost || '',
    maxCost: initialFilters.maxCost || '',
    from: initialFilters.from || '',
    to: initialFilters.to || ''
  });

  const [showAdvanced, setShowAdvanced] = useState(false);

  const handleChange = (field, value) => {
    const newFilters = { ...filters, [field]: value };
    setFilters(newFilters);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const cleanFilters = Object.fromEntries(
      Object.entries(filters).filter(([_, value]) => value !== '')
    );
    onFilterChange(cleanFilters);
  };

  const handleClear = () => {
    const emptyFilters = {
      q: '', status: '', currency: '', minCost: '', maxCost: '', from: '', to: ''
    };
    setFilters(emptyFilters);
    onFilterChange({});
  };

  const hasActiveFilters = Object.values(filters).some(v => v !== '');

  return (
    <div className="bg-slate-800/40 backdrop-blur-xl rounded-2xl shadow-2xl p-7 mb-8 border border-slate-700/50 animate-slide-up">
      <form onSubmit={handleSubmit}>
        {/* Búsqueda básica */}
        <div className="flex flex-wrap items-stretch gap-4 mb-4">
          <div className="flex-1 min-w-0 relative group">
            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 w-5 h-5 text-slate-400 group-focus-within:text-blue-400 transition-colors" />
            <input
              type="text"
              placeholder="🔍 Buscar por número de ticket o solicitante..."
              value={filters.q}
              onChange={(e) => handleChange('q', e.target.value)}
              className="w-full pl-12 pr-4 py-3.5 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-medium text-slate-100 placeholder-slate-500"
            />
          </div>
          <button
            type="button"
            onClick={() => setShowAdvanced(!showAdvanced)}
            className={`px-7 py-3.5 rounded-xl font-bold transition-all flex items-center gap-2 shadow-lg shrink-0 ${
              showAdvanced 
                ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white scale-105 shadow-blue-500/30' 
                : 'bg-slate-700/50 text-slate-200 hover:bg-slate-700 border border-slate-600'
            }`}
          >
            <Filter className="w-5 h-5" />
            Filtros {showAdvanced && '✓'}
          </button>
          <button
            type="submit"
            className="px-10 py-3.5 bg-gradient-to-r from-blue-600 to-blue-700 text-white rounded-xl font-bold hover:from-blue-500 hover:to-blue-600 transition-all shadow-lg shadow-blue-500/30 hover:shadow-blue-500/50 hover:scale-105 shrink-0"
          >
            Buscar
          </button>
        </div>

        {/* Filtros avanzados */}
        {showAdvanced && (
          <div className="space-y-5 pt-6 border-t border-slate-700/50 animate-fade-in">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
              {/* Estado */}
              <div>
                <label className="block text-sm font-bold text-slate-300 mb-2">Estado</label>
                <select
                  value={filters.status}
                  onChange={(e) => handleChange('status', e.target.value)}
                  className="w-full px-4 py-3 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-medium text-slate-100"
                >
                  <option value="" className="bg-slate-900">Todos los estados</option>
                  {Object.values(TICKET_STATUS).map(status => (
                    <option key={status} value={status} className="bg-slate-900">
                      {STATUS_LABELS[status]}
                    </option>
                  ))}
                </select>
              </div>

              {/* Moneda */}
              <div>
                <label className="block text-sm font-bold text-slate-300 mb-2">Moneda</label>
                <select
                  value={filters.currency}
                  onChange={(e) => handleChange('currency', e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="">Todas</option>
                  {Object.values(CURRENCY).map(curr => (
                    <option key={curr} value={curr}>{curr}</option>
                  ))}
                </select>
              </div>

              {/* Costo mínimo */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Costo Mínimo</label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  placeholder="0.00"
                  value={filters.minCost}
                  onChange={(e) => handleChange('minCost', e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              {/* Costo máximo */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Costo Máximo</label>
                <input
                  type="number"
                  min="0"
                  step="0.01"
                  placeholder="9999.99"
                  value={filters.maxCost}
                  onChange={(e) => handleChange('maxCost', e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              {/* Fecha desde */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Desde</label>
                <input
                  type="datetime-local"
                  value={filters.from}
                  onChange={(e) => handleChange('from', e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              {/* Fecha hasta */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Hasta</label>
                <input
                  type="datetime-local"
                  value={filters.to}
                  onChange={(e) => handleChange('to', e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>
            </div>

            {/* Botón limpiar */}
            {hasActiveFilters && (
              <button
                type="button"
                onClick={handleClear}
                className="flex items-center gap-2 px-4 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
              >
                <X className="w-4 h-4" />
                Limpiar filtros
              </button>
            )}
          </div>
        )}
      </form>
    </div>
  );
};

export default FilterBar;