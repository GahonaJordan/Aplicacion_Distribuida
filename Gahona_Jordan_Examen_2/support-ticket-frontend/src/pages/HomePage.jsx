import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Plus, RefreshCw } from 'lucide-react';
import FilterBar from '../components/FilterBar';
import TicketList from '../components/TicketList';
import Pagination from '../components/Pagination';
import ErrorMessage from '../components/ErrorMessage';
import { ticketService } from '../services/ticketService';

const HomePage = () => {
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [filters, setFilters] = useState({});
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalPages: 0,
    totalElements: 0,
    size: 9
  });

  const loadTickets = async (newFilters = filters, page = 0) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await ticketService.getTickets({
        ...newFilters,
        page,
        size: pagination.size
      });

      setTickets(response.content);
      setPagination({
        currentPage: response.pageable.pageNumber,
        totalPages: response.totalPages,
        totalElements: response.totalElements,
        size: response.pageable.pageSize
      });
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadTickets();
  }, []);

  const handleFilterChange = (newFilters) => {
    setFilters(newFilters);
    loadTickets(newFilters, 0);
  };

  const handlePageChange = (page) => {
    loadTickets(filters, page);
  };

  const handleRefresh = () => {
    loadTickets(filters, pagination.currentPage);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950 relative overflow-hidden">
      {/* Decorative background elements */}
      <div className="absolute top-0 left-0 w-96 h-96 bg-blue-600 rounded-full mix-blend-screen filter blur-3xl opacity-10 animate-blob"></div>
      <div className="absolute top-0 right-0 w-96 h-96 bg-purple-600 rounded-full mix-blend-screen filter blur-3xl opacity-10 animate-blob animation-delay-2000"></div>
      <div className="absolute bottom-0 left-1/2 w-96 h-96 bg-pink-600 rounded-full mix-blend-screen filter blur-3xl opacity-10 animate-blob animation-delay-4000"></div>
      
      {/* Grid pattern overlay */}
      <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGRlZnM+PHBhdHRlcm4gaWQ9ImdyaWQiIHdpZHRoPSI2MCIgaGVpZ2h0PSI2MCIgcGF0dGVyblVuaXRzPSJ1c2VyU3BhY2VPblVzZSI+PHBhdGggZD0iTSAxMCAwIEwgMCAwIDAgMTAiIGZpbGw9Im5vbmUiIHN0cm9rZT0icmdiYSgyNTUsMjU1LDI1NSwwLjAzKSIgc3Ryb2tlLXdpZHRoPSIxIi8+PC9wYXR0ZXJuPjwvZGVmcz48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSJ1cmwoI2dyaWQpIi8+PC9zdmc+')] opacity-40"></div>
      
      <div className="max-w-7xl mx-auto px-4 py-8 relative z-10">
        {/* Header */}
        <div className="mb-10 animate-slide-up">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h1 className="text-6xl font-black bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400 bg-clip-text text-transparent mb-3 drop-shadow-2xl">
                Tickets de Soporte
              </h1>
              <p className="text-lg text-slate-400 font-semibold">
                ✨ {pagination.totalElements} ticket{pagination.totalElements !== 1 ? 's' : ''} encontrado{pagination.totalElements !== 1 ? 's' : ''}
              </p>
            </div>
            <div className="flex gap-3">
              <button
                onClick={handleRefresh}
                disabled={loading}
                className="flex items-center gap-2 px-6 py-3.5 bg-slate-800/80 backdrop-blur-sm border border-slate-700 text-slate-200 rounded-xl hover:bg-slate-700/80 hover:border-blue-500/50 transition-all shadow-lg hover:shadow-blue-500/20 disabled:opacity-50 font-bold"
              >
                <RefreshCw className={`w-5 h-5 ${loading ? 'animate-spin text-blue-400' : ''}`} />
                Actualizar
              </button>
              <Link
                to="/create"
                className="flex items-center gap-2 px-8 py-3.5 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-xl hover:from-blue-500 hover:to-purple-500 transition-all shadow-lg shadow-purple-500/30 hover:shadow-purple-500/50 hover:scale-105 font-black"
              >
                <Plus className="w-5 h-5" />
                Nuevo Ticket
              </Link>
            </div>
          </div>
        </div>

        {/* Filtros */}
        <FilterBar onFilterChange={handleFilterChange} initialFilters={filters} />

        {/* Error */}
        {error && <ErrorMessage message={error} onClose={() => setError(null)} />}

        {/* Lista de Tickets */}
        <TicketList tickets={tickets} loading={loading} />

        {/* Paginación */}
        {!loading && tickets.length > 0 && (
          <Pagination
            currentPage={pagination.currentPage}
            totalPages={pagination.totalPages}
            onPageChange={handlePageChange}
          />
        )}
      </div>
    </div>
  );
};

export default HomePage;