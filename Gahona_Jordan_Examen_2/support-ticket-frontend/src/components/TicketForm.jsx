import React, { useState } from 'react';
import { Save, Loader } from 'lucide-react';
import { TICKET_STATUS, PRIORITY, CURRENCY, CATEGORY } from '../utils/constants';
import { generateTicketNumber } from '../utils/formatters';
import ErrorMessage from './ErrorMessage';

const TicketForm = ({ onSubmit, loading, error, onErrorClose }) => {
  const [formData, setFormData] = useState({
    ticketNumber: generateTicketNumber(),
    requesterName: '',
    status: 'OPEN',
    priority: 'MEDIUM',
    category: 'SOFTWARE',
    estimatedCost: '',
    currency: 'USD',
    dueDate: ''
  });

  const [validationErrors, setValidationErrors] = useState({});

  const handleChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
    // Limpiar error de validación del campo
    if (validationErrors[field]) {
      setValidationErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[field];
        return newErrors;
      });
    }
  };

  const validate = () => {
    const errors = {};

    if (!formData.ticketNumber.match(/^ST-\d{4}-\d{6}$/)) {
      errors.ticketNumber = 'El número de ticket debe seguir el formato ST-YYYY-NNNNNN';
    }

    if (formData.requesterName.length < 3 || formData.requesterName.length > 100) {
      errors.requesterName = 'El nombre debe tener entre 3 y 100 caracteres';
    }

    if (!formData.estimatedCost || parseFloat(formData.estimatedCost) <= 0) {
      errors.estimatedCost = 'El costo debe ser mayor a 0';
    }

    if (!formData.dueDate) {
      errors.dueDate = 'La fecha de vencimiento es requerida';
    } else {
      const dueDate = new Date(formData.dueDate);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
      if (dueDate <= today) {
        errors.dueDate = 'La fecha de vencimiento debe ser futura';
      }
    }

    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    const errors = validate();
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      return;
    }

    const submitData = {
      ...formData,
      estimatedCost: parseFloat(formData.estimatedCost)
    };

    await onSubmit(submitData);
  };

  const handleGenerateNewNumber = () => {
    handleChange('ticketNumber', generateTicketNumber());
  };

  return (
    <form onSubmit={handleSubmit} className="bg-slate-800/40 backdrop-blur-xl rounded-3xl shadow-2xl p-10 border border-slate-700/50 animate-scale-in">
      <div className="flex items-center gap-3 mb-8 pb-6 border-b border-slate-700/50">
        <div className="p-3 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl shadow-lg shadow-blue-500/30">
          <Save className="w-7 h-7 text-white" />
        </div>
        <h2 className="text-3xl font-black text-slate-100">Información del Ticket</h2>
      </div>

      {error && <ErrorMessage message={error} onClose={onErrorClose} />}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Número de Ticket */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-blue-500 rounded-full shadow-lg shadow-blue-500/50"></span>
            Número de Ticket *
          </label>
          <div className="flex gap-3">
            <input
              type="text"
              value={formData.ticketNumber}
              onChange={(e) => handleChange('ticketNumber', e.target.value)}
              className={`flex-1 px-5 py-3.5 bg-slate-900/50 border-2 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 placeholder-slate-500 ${
                validationErrors.ticketNumber ? 'border-red-500 bg-red-900/20' : 'border-slate-700'
              }`}
              placeholder="ST-2025-000001"
            />
            <button
              type="button"
              onClick={handleGenerateNewNumber}
              className="px-5 py-3.5 bg-gradient-to-r from-purple-600 to-purple-700 text-white rounded-xl hover:from-purple-500 hover:to-purple-600 transition-all font-bold shadow-lg shadow-purple-500/30 hover:shadow-purple-500/50"
            >
              🎲 Generar
            </button>
          </div>
          {validationErrors.ticketNumber && (
            <p className="text-red-500 text-sm mt-1">{validationErrors.ticketNumber}</p>
          )}
        </div>

        {/* Solicitante */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-green-500 rounded-full shadow-lg shadow-green-500/50"></span>
            Nombre del Solicitante *
          </label>
          <input
            type="text"
            value={formData.requesterName}
            onChange={(e) => handleChange('requesterName', e.target.value)}
            className={`w-full px-5 py-3.5 bg-slate-900/50 border-2 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-medium text-slate-100 placeholder-slate-500 ${
              validationErrors.requesterName ? 'border-red-500 bg-red-900/20' : 'border-slate-700'
            }`}
            placeholder="👤 Juan Pérez"
          />
          {validationErrors.requesterName && (
            <p className="text-red-500 text-sm mt-1">{validationErrors.requesterName}</p>
          )}
        </div>

        {/* Estado */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-cyan-500 rounded-full shadow-lg shadow-cyan-500/50"></span>
            Estado *
          </label>
          <select
            value={formData.status}
            onChange={(e) => handleChange('status', e.target.value)}
            className="w-full px-5 py-3.5 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 cursor-pointer"
          >
            {Object.values(TICKET_STATUS).map(status => (
              <option key={status} value={status}>{status.replace('_', ' ')}</option>
            ))}
          </select>
        </div>

        {/* Prioridad */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-orange-500 rounded-full shadow-lg shadow-orange-500/50"></span>
            Prioridad *
          </label>
          <select
            value={formData.priority}
            onChange={(e) => handleChange('priority', e.target.value)}
            className="w-full px-5 py-3.5 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 cursor-pointer"
          >
            {Object.values(PRIORITY).map(priority => (
              <option key={priority} value={priority}>{priority}</option>
            ))}
          </select>
        </div>

        {/* Categoría */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-purple-500 rounded-full shadow-lg shadow-purple-500/50"></span>
            Categoría *
          </label>
          <select
            value={formData.category}
            onChange={(e) => handleChange('category', e.target.value)}
            className="w-full px-5 py-3.5 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 cursor-pointer"
          >
            {Object.values(CATEGORY).map(category => (
              <option key={category} value={category}>{category}</option>
            ))}
          </select>
        </div>

        {/* Costo Estimado */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-emerald-500 rounded-full shadow-lg shadow-emerald-500/50"></span>
            Costo Estimado *
          </label>
          <input
            type="number"
            step="0.01"
            min="0"
            value={formData.estimatedCost}
            onChange={(e) => handleChange('estimatedCost', e.target.value)}
            className={`w-full px-5 py-3.5 bg-slate-900/50 border-2 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 placeholder-slate-500 ${
              validationErrors.estimatedCost ? 'border-red-500 bg-red-900/20' : 'border-slate-700'
            }`}
            placeholder="💵 150.50"
          />
          {validationErrors.estimatedCost && (
            <p className="text-red-500 text-sm mt-1">{validationErrors.estimatedCost}</p>
          )}
        </div>

        {/* Moneda */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-yellow-500 rounded-full shadow-lg shadow-yellow-500/50"></span>
            Moneda *
          </label>
          <select
            value={formData.currency}
            onChange={(e) => handleChange('currency', e.target.value)}
            className="w-full px-5 py-3.5 bg-slate-900/50 border-2 border-slate-700 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 cursor-pointer"
          >
            {Object.values(CURRENCY).map(currency => (
              <option key={currency} value={currency}>{currency}</option>
            ))}
          </select>
        </div>

        {/* Fecha de Vencimiento */}
        <div>
          <label className="block text-sm font-bold text-slate-300 mb-3 flex items-center gap-2">
            <span className="w-2 h-2 bg-red-500 rounded-full shadow-lg shadow-red-500/50"></span>
            Fecha de Vencimiento *
          </label>
          <input
            type="date"
            value={formData.dueDate}
            onChange={(e) => handleChange('dueDate', e.target.value)}
            min={new Date().toISOString().split('T')[0]}
            className={`w-full px-5 py-3.5 bg-slate-900/50 border-2 rounded-xl focus:ring-4 focus:ring-blue-500/30 focus:border-blue-500 transition-all font-semibold text-slate-100 ${
              validationErrors.dueDate ? 'border-red-500 bg-red-900/20' : 'border-slate-700'
            }`}
          />
          {validationErrors.dueDate && (
            <p className="text-red-500 text-sm mt-1">{validationErrors.dueDate}</p>
          )}
        </div>
      </div>

      {/* Botón Submit */}
      <div className="mt-10 pt-8 border-t border-slate-700/50">
        <button
          type="submit"
          disabled={loading}
          className="w-full flex items-center justify-center gap-3 px-8 py-5 bg-gradient-to-r from-blue-600 via-purple-600 to-pink-600 text-white rounded-2xl font-black text-lg hover:from-blue-500 hover:via-purple-500 hover:to-pink-500 disabled:opacity-50 disabled:cursor-not-allowed transition-all shadow-2xl shadow-blue-500/30 hover:shadow-purple-500/50 hover:scale-105 transform"
        >
          {loading ? (
            <>
              <Loader className="w-6 h-6 animate-spin" />
              <span>Creando Ticket...</span>
            </>
          ) : (
            <>
              <Save className="w-6 h-6" />
              <span>✨ Crear Ticket Ahora</span>
            </>
          )}
        </button>
      </div>
    </form>
  );
};

export default TicketForm;