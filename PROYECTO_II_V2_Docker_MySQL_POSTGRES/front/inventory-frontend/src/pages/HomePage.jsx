import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Package, Users, Warehouse, ShoppingCart, Boxes } from 'lucide-react';
import { productoService } from '../services/productoService';
import { proveedorService } from '../services/proveedorService';
import { bodegaService } from '../services/bodegaService';
import { ordenService } from '../services/ordenService';
import { inventarioService } from '../services/inventarioService';

export default function HomePage() {
  const [stats, setStats] = useState({
    productos: 0,
    proveedores: 0,
    bodegas: 0,
    inventario: 0,
    ordenesPendientes: 0
  });
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({
    productos: false,
    proveedores: false,
    bodegas: false,
    inventario: false,
    ordenes: false
  });

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      console.log('Iniciando carga de estadísticas...');
      
      const errorStates = { productos: false, proveedores: false, bodegas: false, inventario: false, ordenes: false };
      
      const [productos, proveedores, bodegas, inventario, ordenes] = await Promise.all([
        productoService.getAll().catch(err => {
          console.error('Error en productos:', err);
          errorStates.productos = true;
          return [];
        }),
        proveedorService.getAll().catch(err => {
          console.error('Error en proveedores:', err);
          errorStates.proveedores = true;
          return [];
        }),
        bodegaService.getAll().catch(err => {
          console.error('Error en bodegas:', err);
          errorStates.bodegas = true;
          return [];
        }),
        inventarioService.getAll().catch(err => {
          console.error('Error en inventario:', err);
          errorStates.inventario = true;
          return [];
        }),
        ordenService.getAll().catch(err => {
          console.error('Error en ordenes:', err);
          errorStates.ordenes = true;
          return [];
        })
      ]);

      console.log('Datos recibidos:', { productos, proveedores, bodegas, inventario, ordenes });
      setErrors(errorStates);

      setStats({
        productos: productos.length,
        proveedores: proveedores.length,
        bodegas: bodegas.length,
        inventario: inventario.length,
        ordenesPendientes: ordenes.filter(o => o.estado === 'PENDIENTE').length
      });
    } catch (error) {
      console.error('Error cargando estadísticas:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div>
      {(errors.productos || errors.proveedores || errors.bodegas || errors.ordenes) && (
        <div className="card" style={{ 
          backgroundColor: '#fef3c7', 
          borderLeft: '4px solid var(--warning)',
          marginBottom: '1rem'
        }}>
          <h4 style={{ color: '#92400e', marginBottom: '0.5rem' }}>⚠️ Advertencia de Conexión</h4>
          <p style={{ color: '#78350f', fontSize: '0.9rem' }}>
            Algunos servicios no están respondiendo a través del API Gateway:
          </p>
          <ul style={{ color: '#78350f', fontSize: '0.9rem', marginTop: '0.5rem', marginLeft: '1.5rem' }}>
            {errors.productos && <li>Servicio de Productos</li>}
            {errors.proveedores && <li>Servicio de Proveedores</li>}
            {errors.bodegas && <li>Servicio de Inventario/Bodegas</li>}
            {errors.inventario && <li>Servicio de Inventario</li>}
            {errors.ordenes && <li>Servicio de Órdenes</li>}
          </ul>
          <p style={{ color: '#78350f', fontSize: '0.85rem', marginTop: '0.5rem' }}>
            Verifica que el API Gateway (puerto 8080) y los microservicios estén corriendo. Revisa la consola del navegador para más detalles.
          </p>
        </div>
      )}

      <div className="card">
        <h1>Bienvenido al Sistema de Gestión de Inventario</h1>
        <p style={{ color: 'var(--gray-700)', marginTop: '0.5rem' }}>
          Sistema de gestión integral de productos, proveedores, inventario y órdenes de compra
        </p>
      </div>

      <div className="stats-grid">
        <Link to="/productos" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="stat-card" style={{ borderLeft: '4px solid var(--primary)' }}>
            <div className="stat-label">
              <Package size={20} style={{ display: 'inline', marginRight: '8px' }} />
              Total Productos
            </div>
            <div className="stat-value">{stats.productos}</div>
          </div>
        </Link>

        <Link to="/proveedores" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="stat-card" style={{ borderLeft: '4px solid var(--success)' }}>
            <div className="stat-label">
              <Users size={20} style={{ display: 'inline', marginRight: '8px' }} />
              Total Proveedores
            </div>
            <div className="stat-value">{stats.proveedores}</div>
          </div>
        </Link>

        <Link to="/bodegas" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="stat-card" style={{ borderLeft: '4px solid var(--warning)' }}>
            <div className="stat-label">
              <Warehouse size={20} style={{ display: 'inline', marginRight: '8px' }} />
              Total Bodegas
            </div>
            <div className="stat-value">{stats.bodegas}</div>
          </div>
        </Link>

        <Link to="/inventario" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="stat-card" style={{ borderLeft: '4px solid var(--info)' }}>
            <div className="stat-label">
              <Boxes size={20} style={{ display: 'inline', marginRight: '8px' }} />
              Registros de Inventario
            </div>
            <div className="stat-value">{stats.inventario}</div>
          </div>
        </Link>

        <Link to="/ordenes" style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="stat-card" style={{ borderLeft: '4px solid var(--danger)' }}>
            <div className="stat-label">
              <ShoppingCart size={20} style={{ display: 'inline', marginRight: '8px' }} />
              Órdenes Pendientes
            </div>
            <div className="stat-value">{stats.ordenesPendientes}</div>
          </div>
        </Link>
      </div>

      <div className="grid grid-cols-2">
        <div className="card">
          <h3 style={{ marginBottom: '1rem' }}>Accesos Rápidos</h3>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
            <Link to="/productos" className="btn btn-secondary" style={{ textDecoration: 'none' }}>
              <Package size={18} />
              Gestionar Productos
            </Link>
            <Link to="/proveedores" className="btn btn-secondary" style={{ textDecoration: 'none' }}>
              <Users size={18} />
              Gestionar Proveedores
            </Link>
            <Link to="/bodegas" className="btn btn-secondary" style={{ textDecoration: 'none' }}>
              <Warehouse size={18} />
              Gestionar Bodegas
            </Link>
            <Link to="/inventario" className="btn btn-secondary" style={{ textDecoration: 'none' }}>
              <Boxes size={18} />
              Ver Inventario
            </Link>
            <Link to="/ordenes" className="btn btn-primary" style={{ textDecoration: 'none' }}>
              <ShoppingCart size={18} />
              Nueva Orden de Compra
            </Link>
          </div>
        </div>

        <div className="card">
          <h3 style={{ marginBottom: '1rem' }}>Información del Sistema</h3>
          <ul style={{ listStyle: 'none', color: 'var(--gray-700)', lineHeight: '1.8' }}>
            <li>Arquitectura de Microservicios</li>
            <li>4 Servicios Independientes</li>
            <li>Base de datos por servicio</li>
            <li>API REST completa</li>
            <li>Actualización automática de inventario</li>
          </ul>
        </div>
      </div>
    </div>
  );
}