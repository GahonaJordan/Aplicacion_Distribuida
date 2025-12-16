import React from 'react';
import { Calendar, DollarSign, User, Tag, Clock } from 'lucide-react';
import { formatDate, formatDateTime, formatCurrency } from '../utils/formatters';
import { STATUS_COLORS, PRIORITY_COLORS, STATUS_LABELS, PRIORITY_LABELS, CATEGORY_LABELS } from '../utils/constants';

const TicketCard = ({ ticket }) => {
  return (
    <div className="group relative bg-slate-800/40 backdrop-blur-xl rounded-3xl shadow-xl hover:shadow-2xl transition-all duration-500 p-7 border border-slate-700/50 hover:border-blue-500/50 hover:-translate-y-3 animate-scale-in overflow-hidden">
      {/* Decorative corner gradient */}
      <div className="absolute top-0 right-0 w-40 h-40 bg-gradient-to-br from-blue-600/20 to-purple-600/20 rounded-bl-full transform translate-x-10 -translate-y-10 group-hover:scale-150 transition-transform duration-500 blur-2xl"></div>
      
      {/* Shine effect */}
      <div className="absolute inset-0 bg-gradient-to-br from-white/0 via-white/5 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity duration-500"></div>
      
      {/* Header */}
      <div className="flex items-start justify-between mb-6 relative z-10">
        <div className="flex-1">
          <div className="flex items-center gap-3 mb-3">
            <div className="p-2.5 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl shadow-lg shadow-blue-500/30">
              <Tag className="w-5 h-5 text-white" />
            </div>
            <h3 className="text-xl font-black text-slate-100 group-hover:text-blue-400 transition-colors drop-shadow-lg">{ticket.ticketNumber}</h3>
          </div>
          <div className="flex items-center gap-2 ml-1">
            <Clock className="w-4 h-4 text-slate-500" />
            <p className="text-xs font-semibold text-slate-400">{formatDateTime(ticket.createdAt)}</p>
          </div>
        </div>
        <span className={`px-4 py-2 rounded-xl text-xs font-black shadow-lg ${PRIORITY_COLORS[ticket.priority]}`}>
          {PRIORITY_LABELS[ticket.priority]}
        </span>
      </div>

      {/* Información del ticket */}
      <div className="space-y-4 mb-6 relative z-10">
        {/* Solicitante */}
        <div className="flex items-center gap-3 p-4 bg-gradient-to-r from-slate-700/50 to-blue-900/30 rounded-2xl group-hover:from-blue-900/40 group-hover:to-purple-900/30 transition-all border border-slate-600/30 backdrop-blur-sm">
          <div className="p-3 bg-slate-900/50 rounded-xl shadow-lg border border-slate-600/30">
            <User className="w-6 h-6 text-blue-400" />
          </div>
          <div className="flex-1">
            <p className="text-xs font-bold text-blue-300 mb-1">👤 SOLICITANTE</p>
            <span className="text-base font-black text-slate-100">{ticket.requesterName}</span>
          </div>
        </div>

        {/* Categoría y Costo */}
        <div className="grid grid-cols-2 gap-3">
          <div className="flex flex-col gap-2 p-4 bg-gradient-to-br from-purple-900/30 to-pink-900/30 rounded-2xl border border-purple-600/30 backdrop-blur-sm">
            <div className="flex items-center gap-2">
              <Tag className="w-5 h-5 text-purple-400" />
              <p className="text-xs font-bold text-purple-300">CATEGORÍA</p>
            </div>
            <span className="text-sm font-black text-slate-100">{CATEGORY_LABELS[ticket.category] || ticket.category}</span>
          </div>
          
          <div className="flex flex-col gap-2 p-4 bg-gradient-to-br from-emerald-900/30 to-green-900/30 rounded-2xl border border-emerald-600/30 backdrop-blur-sm">
            <div className="flex items-center gap-2">
              <DollarSign className="w-5 h-5 text-emerald-400" />
              <p className="text-xs font-bold text-emerald-300">COSTO</p>
            </div>
            <span className="text-base font-black text-slate-100">
              {formatCurrency(ticket.estimatedCost, ticket.currency)}
            </span>
          </div>
        </div>

        {/* Fecha límite */}
        <div className="flex items-center gap-3 p-4 bg-gradient-to-r from-orange-900/30 to-red-900/30 rounded-2xl border border-orange-600/30 backdrop-blur-sm">
          <div className="p-2 bg-slate-900/50 rounded-xl shadow-lg border border-slate-600/30">
            <Calendar className="w-6 h-6 text-orange-400" />
          </div>
          <div>
            <p className="text-xs font-bold text-orange-300 mb-1">⏰ VENCIMIENTO</p>
            <span className="text-sm font-black text-slate-100">{formatDate(ticket.dueDate)}</span>
          </div>
        </div>
      </div>

      {/* Estado */}
      <div className="pt-5 border-t border-slate-700/50 relative z-10">
        <span className={`inline-flex items-center justify-center w-full px-5 py-4 rounded-2xl text-sm font-black uppercase tracking-wide ${STATUS_COLORS[ticket.status]} transition-all group-hover:scale-105 transform`}>
          ✨ {STATUS_LABELS[ticket.status]}
        </span>
      </div>
    </div>
  );
};

export default TicketCard;