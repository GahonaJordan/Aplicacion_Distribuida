import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, CheckCircle } from 'lucide-react';
import TicketForm from '../components/TicketForm';
import { ticketService } from '../services/ticketService';

const CreateTicketPage = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (formData) => {
    try {
      setLoading(true);
      setError(null);
      
      await ticketService.createTicket(formData);
      
      setSuccess(true);
      
      // Redirigir después de 2 segundos
      setTimeout(() => {
        navigate('/');
      }, 2000);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (success) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950 flex items-center justify-center p-4">
        <div className="bg-slate-800/40 backdrop-blur-xl rounded-3xl shadow-2xl p-14 text-center max-w-md animate-scale-in border border-slate-700/50">
          <div className="w-24 h-24 bg-gradient-to-br from-green-500 to-emerald-600 rounded-full flex items-center justify-center mx-auto mb-8 shadow-2xl shadow-green-500/50 animate-bounce">
            <CheckCircle className="w-14 h-14 text-white" />
          </div>
          <h2 className="text-4xl font-black text-slate-100 mb-4">
            ¡Ticket Creado!
          </h2>
          <p className="text-slate-400 mb-8 text-lg">
            ✨ El ticket se ha creado exitosamente. Redirigiendo...
          </p>
          <Link
            to="/"
            className="inline-block px-8 py-4 bg-gradient-to-r from-blue-600 to-purple-600 text-white rounded-xl hover:from-blue-500 hover:to-purple-500 transition-all shadow-lg shadow-blue-500/30 hover:shadow-purple-500/50 font-bold text-lg hover:scale-105"
          >
            Ir a la lista
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-950 via-slate-900 to-slate-950 relative overflow-hidden">
      {/* Decorative background */}
      <div className="absolute top-0 left-0 w-96 h-96 bg-blue-600 rounded-full mix-blend-screen filter blur-3xl opacity-10 animate-blob"></div>
      <div className="absolute bottom-0 right-0 w-96 h-96 bg-purple-600 rounded-full mix-blend-screen filter blur-3xl opacity-10 animate-blob animation-delay-2000"></div>
      
      <div className="max-w-4xl mx-auto px-4 py-8 relative z-10">
        {/* Header */}
        <div className="mb-10 animate-slide-up">
          <Link
            to="/"
            className="inline-flex items-center gap-2 text-blue-400 hover:text-blue-300 mb-6 transition-all font-bold text-lg hover:gap-3 group"
          >
            <ArrowLeft className="w-6 h-6 group-hover:-translate-x-1 transition-transform" />
            Volver a la lista
          </Link>
          <h1 className="text-5xl font-black bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400 bg-clip-text text-transparent mb-3 drop-shadow-2xl">
            Crear Nuevo Ticket
          </h1>
          <p className="text-lg text-slate-400 font-medium">
            ✨ Completa el formulario para registrar un nuevo ticket de soporte
          </p>
        </div>

        {/* Formulario */}
        <TicketForm
          onSubmit={handleSubmit}
          loading={loading}
          error={error}
          onErrorClose={() => setError(null)}
        />
      </div>
    </div>
  );
};

export default CreateTicketPage;