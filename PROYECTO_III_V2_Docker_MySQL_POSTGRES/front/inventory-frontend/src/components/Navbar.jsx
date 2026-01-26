import { Link, useLocation } from 'react-router-dom';
import { Package, Users, Warehouse, ShoppingCart, Home, Boxes, LogOut, UserCog } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { hasAdminAccess, isAdmin } from '../utils/roleUtils';

export default function Navbar() {
  const location = useLocation();
  const { user, logout } = useAuth();

  const isActive = (path) => location.pathname === path;
  const userHasAdminAccess = hasAdminAccess(user);
  const userIsAdmin = isAdmin(user);

  const handleLogout = () => {
    logout();
    window.location.reload();
  };

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <Link to="/" className="navbar-brand">
          📦 Sistema de Inventario
        </Link>
        
        <ul className="navbar-nav">
          <li>
            <Link 
              to="/" 
              className={`nav-link ${isActive('/') ? 'active' : ''}`}
            >
              <Home size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Inicio
            </Link>
          </li>

          {/* Productos - visible para todos */}
          <li>
            <Link 
              to="/productos" 
              className={`nav-link ${isActive('/productos') ? 'active' : ''}`}
            >
              <Package size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Productos
            </Link>
          </li>

          {/* Proveedores - visible para todos */}
          <li>
            <Link 
              to="/proveedores" 
              className={`nav-link ${isActive('/proveedores') ? 'active' : ''}`}
            >
              <Users size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Proveedores
            </Link>
          </li>

          {/* Bodegas - solo administradores */}
          {userHasAdminAccess && (
            <li>
              <Link 
                to="/bodegas" 
                className={`nav-link ${isActive('/bodegas') ? 'active' : ''}`}
              >
                <Warehouse size={18} style={{ display: 'inline', marginRight: '4px' }} />
                Bodegas
              </Link>
            </li>
          )}

          {/* Inventario - visible para todos */}
          <li>
            <Link 
              to="/inventario" 
              className={`nav-link ${isActive('/inventario') ? 'active' : ''}`}
            >
              <Boxes size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Inventario
            </Link>
          </li>

          {/* Órdenes - visible para todos */}
          <li>
            <Link 
              to="/ordenes" 
              className={`nav-link ${isActive('/ordenes') ? 'active' : ''}`}
            >
              <ShoppingCart size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Órdenes
            </Link>
          </li>

          {/* Gestión de Usuarios - solo administradores */}
          {userIsAdmin && (
            <li>
              <Link 
                to="/usuarios" 
                className={`nav-link ${isActive('/usuarios') ? 'active' : ''}`}
              >
                <UserCog size={18} style={{ display: 'inline', marginRight: '4px' }} />
                Usuarios
              </Link>
            </li>
          )}

          <li className="navbar-user">
            <span className="user-info">{user?.username}</span>
            <button 
              onClick={handleLogout}
              className="nav-link logout-btn"
            >
              <LogOut size={18} style={{ display: 'inline', marginRight: '4px' }} />
              Salir
            </button>
          </li>
        </ul>
      </div>
    </nav>
  );
}