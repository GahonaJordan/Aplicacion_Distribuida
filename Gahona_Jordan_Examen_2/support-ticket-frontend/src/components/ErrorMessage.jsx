import React from 'react';
import { AlertCircle, X } from 'lucide-react';

const ErrorMessage = ({ message, onClose }) => {
  if (!message) return null;

  return (
    <div className="bg-gradient-to-r from-red-900/40 to-rose-900/40 backdrop-blur-sm border-l-4 border-red-500 p-5 rounded-2xl mb-8 animate-fade-in shadow-lg shadow-red-500/20">
      <div className="flex items-start">
        <div className="p-2 bg-red-500/20 rounded-xl border border-red-500/30">
          <AlertCircle className="w-6 h-6 text-red-400" />
        </div>
        <div className="ml-4 flex-1">
          <h3 className="text-base font-black text-red-300 mb-1">⚠️ Error</h3>
          <p className="text-sm text-red-200 font-semibold">{message}</p>
        </div>
        {onClose && (
          <button 
            onClick={onClose}
            className="ml-3 p-2 text-red-400 hover:text-red-300 hover:bg-red-500/20 rounded-xl transition-all"
          >
            <X className="w-5 h-5" />
          </button>
        )}
      </div>
    </div>
  );
};

export default ErrorMessage;