import React from 'react';
import TicketCard from './TicketCard';
import { FileQuestion } from 'lucide-react';

const TicketList = ({ tickets, loading }) => {
  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[1, 2, 3, 4, 5, 6].map(i => (
          <div key={i} className="bg-gray-200 rounded-xl h-64 animate-pulse" />
        ))}
      </div>
    );
  }

  if (!tickets || tickets.length === 0) {
    return (
      <div className="bg-slate-800/40 backdrop-blur-xl rounded-3xl shadow-2xl p-16 text-center border border-slate-700/50 animate-scale-in">
        <div className="w-32 h-32 bg-gradient-to-br from-blue-500/20 to-purple-500/20 rounded-full flex items-center justify-center mx-auto mb-8 shadow-2xl border border-blue-500/30">
          <FileQuestion className="w-16 h-16 text-blue-400" />
        </div>
        <h3 className="text-3xl font-black text-slate-100 mb-4">
          No se encontraron tickets
        </h3>
        <p className="text-lg text-slate-400 mb-8">
          Intenta ajustar los filtros de búsqueda o crea un nuevo ticket para comenzar
        </p>
        <div className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-xl font-bold shadow-lg shadow-blue-500/30">
          🔍 Prueba otra búsqueda
        </div>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {tickets.map(ticket => (
        <TicketCard key={ticket.id} ticket={ticket} />
      ))}
    </div>
  );
};

export default TicketList;