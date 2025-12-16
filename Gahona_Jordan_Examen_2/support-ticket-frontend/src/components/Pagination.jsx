import React from 'react';
import { ChevronLeft, ChevronRight } from 'lucide-react';

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
  if (totalPages <= 1) return null;

  const getPageNumbers = () => {
    const pages = [];
    const maxVisible = 5;
    
    let start = Math.max(0, currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(totalPages - 1, start + maxVisible - 1);
    
    if (end - start < maxVisible - 1) {
      start = Math.max(0, end - maxVisible + 1);
    }
    
    for (let i = start; i <= end; i++) {
      pages.push(i);
    }
    
    return pages;
  };

  return (
    <div className="flex items-center justify-center gap-3 mt-10 animate-fade-in">
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        className="p-3 rounded-xl bg-slate-800/50 backdrop-blur-sm border border-slate-700 hover:border-blue-500 hover:bg-slate-700/50 disabled:opacity-30 disabled:cursor-not-allowed transition-all shadow-lg hover:shadow-blue-500/20 group"
      >
        <ChevronLeft className="w-6 h-6 text-slate-400 group-hover:text-blue-400 transition-colors" />
      </button>
      
      <div className="flex gap-2">
        {getPageNumbers().map(page => (
          <button
            key={page}
            onClick={() => onPageChange(page)}
            className={`px-5 py-3 rounded-xl font-bold transition-all shadow-lg transform hover:scale-105 ${
              page === currentPage
                ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white scale-110 shadow-blue-500/50'
                : 'bg-slate-800/50 backdrop-blur-sm border border-slate-700 text-slate-300 hover:border-blue-500 hover:bg-slate-700/50 hover:text-blue-400'
            }`}
          >
            {page + 1}
          </button>
        ))}
      </div>
      
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage === totalPages - 1}
        className="p-3 rounded-xl bg-slate-800/50 backdrop-blur-sm border border-slate-700 hover:border-blue-500 hover:bg-slate-700/50 disabled:opacity-30 disabled:cursor-not-allowed transition-all shadow-lg hover:shadow-blue-500/20 group"
      >
        <ChevronRight className="w-6 h-6 text-slate-400 group-hover:text-blue-400 transition-colors" />
      </button>
    </div>
  );
};

export default Pagination;