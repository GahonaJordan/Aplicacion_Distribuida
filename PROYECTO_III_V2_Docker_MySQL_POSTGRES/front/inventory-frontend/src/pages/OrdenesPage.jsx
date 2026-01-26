import { useState, useEffect } from 'react';
import { Plus, CheckCircle, XCircle, Package, Trash2 } from 'lucide-react';
import { ordenService } from '../services/ordenService';
import { productoService } from '../services/productoService';
import { proveedorService } from '../services/proveedorService';
import { bodegaService } from '../services/bodegaService';

export default function OrdenesPage() {
  const [ordenes, setOrdenes] = useState([]);
  const [productos, setProductos] = useState([]);
  const [proveedores, setProveedores] = useState([]);
  const [bodegas, setBodegas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [alert, setAlert] = useState(null);
  const [detalles, setDetalles] = useState([{
    productoId: '',
    sku: '',
    cantidad: 1,
    precioUnitario: 0
  }]);
  const [formData, setFormData] = useState({
    proveedorId: '',
    bodegaId: '',
    fechaEsperada: '',
    notas: ''
  });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      console.log('Cargando datos para órdenes...');
      
      const [ordenesData, productosData, proveedoresData, bodegasData] = await Promise.all([
        ordenService.getAll().catch(err => {
          console.error('Error cargando órdenes:', err);
          return [];
        }),
        productoService.getAll().then(data => data.filter(p => p.status === 'ACTIVE')).catch(err => {
          console.error('Error cargando productos:', err);
          return [];
        }),
        proveedorService.getAll().then(data => data.filter(p => p.status === 'ACTIVE')).catch(err => {
          console.error('Error cargando proveedores:', err);
          return [];
        }),
        bodegaService.getAll().then(data => data.filter(b => b.status === 'ACTIVE')).catch(err => {
          console.error('Error cargando bodegas:', err);
          return [];
        })
      ]);
      
      console.log('Datos cargados:', { 
        ordenes: ordenesData.length, 
        productos: productosData.length, 
        proveedores: proveedoresData.length, 
        bodegas: bodegasData.length 
      });
      
      setOrdenes(ordenesData);
      setProductos(productosData);
      setProveedores(proveedoresData);
      setBodegas(bodegasData);
    } catch (error) {
      console.error('Error general al cargar datos:', error);
      showAlert('Error al cargar datos: ' + error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, type = 'success') => {
    setAlert({ message, type });
    setTimeout(() => setAlert(null), 5000);
  };

  const handleOpenModal = () => {
    setFormData({
      proveedorId: '',
      bodegaId: '',
      fechaEsperada: '',
      notas: ''
    });
    setDetalles([{
      productoId: '',
      sku: '',
      cantidad: 1,
      precioUnitario: 0
    }]);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleDetalleChange = (index, field, value) => {
    const newDetalles = [...detalles];
    newDetalles[index][field] = value;

    // Si cambia el producto, actualizar SKU y precio
    if (field === 'productoId') {
      const producto = productos.find(p => p.id === parseInt(value));
      if (producto) {
        newDetalles[index].sku = producto.sku;
        newDetalles[index].precioUnitario = producto.price;
      }
    }

    setDetalles(newDetalles);
  };

  const agregarDetalle = () => {
    setDetalles([...detalles, {
      productoId: '',
      sku: '',
      cantidad: 1,
      precioUnitario: 0
    }]);
  };

  const eliminarDetalle = (index) => {
    if (detalles.length > 1) {
      setDetalles(detalles.filter((_, i) => i !== index));
    }
  };

  const calcularTotal = () => {
    return detalles.reduce((sum, det) => {
      return sum + (det.cantidad * det.precioUnitario);
    }, 0);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const ordenData = {
        proveedorId: parseInt(formData.proveedorId),
        bodegaId: parseInt(formData.bodegaId),
        fechaEsperada: formData.fechaEsperada || null,
        notas: formData.notas,
        detalles: detalles.map(det => ({
          productoId: parseInt(det.productoId),
          sku: det.sku,
          cantidad: parseInt(det.cantidad),
          precioUnitario: parseFloat(det.precioUnitario)
        }))
      };

      await ordenService.create(ordenData);
      showAlert('Orden de compra creada exitosamente');
      handleCloseModal();
      loadData();
    } catch (error) {
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const handleAprobar = async (id) => {
    if (!window.confirm('¿Aprobar esta orden de compra?')) return;

    try {
      await ordenService.aprobar(id, 'Orden aprobada');
      showAlert('Orden aprobada exitosamente');
      loadData();
    } catch (error) {
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const handleRecibir = async (id) => {
    if (!window.confirm('¿Confirmar recepción de mercadería? Esto actualizará el inventario.')) return;

    try {
      await ordenService.recibir(id, 'Mercadería recibida en perfectas condiciones');
      showAlert('Orden recibida e inventario actualizado exitosamente', 'success');
      loadData();
    } catch (error) {
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const handleCancelar = async (id) => {
    const motivo = window.prompt('Motivo de cancelación:');
    if (!motivo) return;

    try {
      await ordenService.cancelar(id, motivo);
      showAlert('Orden cancelada');
      loadData();
    } catch (error) {
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const getEstadoBadge = (estado) => {
    const badges = {
      'PENDIENTE': 'badge-warning',
      'APROBADA': 'badge-secondary',
      'RECIBIDA': 'badge-success',
      'CANCELADA': 'badge-danger'
    };
    return badges[estado] || 'badge-secondary';
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
      {alert && (
        <div className={`alert alert-${alert.type}`}>
          {alert.message}
        </div>
      )}

      <div className="card">
        <div className="card-header">
          <h1 className="card-title">Gestión de Órdenes de Compra</h1>
          <button className="btn btn-primary" onClick={handleOpenModal}>
            <Plus size={18} />
            Nueva Orden
          </button>
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Número</th>
                <th>Proveedor</th>
                <th>Bodega</th>
                <th>Fecha Orden</th>
                <th>Monto Total</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {ordenes.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '2rem' }}>
                    No hay órdenes registradas
                  </td>
                </tr>
              ) : (
                ordenes.map(orden => (
                  <tr key={orden.id}>
                    <td><strong>{orden.numeroOrden}</strong></td>
                    <td>{orden.nombreProveedor}</td>
                    <td>{orden.nombreBodega}</td>
                    <td>{new Date(orden.fechaOrden).toLocaleDateString()}</td>
                    <td>${parseFloat(orden.montoTotal).toFixed(2)}</td>
                    <td>
                      <span className={`badge ${getEstadoBadge(orden.estado)}`}>
                        {orden.estado}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        {orden.estado === 'PENDIENTE' && (
                          <button 
                            className="btn btn-sm btn-success"
                            onClick={() => handleAprobar(orden.id)}
                            title="Aprobar"
                          >
                            <CheckCircle size={16} />
                          </button>
                        )}
                        {orden.estado === 'APROBADA' && (
                          <button 
                            className="btn btn-sm btn-primary"
                            onClick={() => handleRecibir(orden.id)}
                            title="Recibir"
                          >
                            <Package size={16} />
                          </button>
                        )}
                        {(orden.estado === 'PENDIENTE' || orden.estado === 'APROBADA') && (
                          <button 
                            className="btn btn-sm btn-danger"
                            onClick={() => handleCancelar(orden.id)}
                            title="Cancelar"
                          >
                            <XCircle size={16} />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={handleCloseModal}>
          <div className="modal" onClick={(e) => e.stopPropagation()} style={{ maxWidth: '800px' }}>
            <div className="modal-header">
              <h2 className="modal-title">Nueva Orden de Compra</h2>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="grid grid-cols-2">
                <div className="form-group">
                  <label className="form-label">Proveedor *</label>
                  <select
                    name="proveedorId"
                    className="form-select"
                    value={formData.proveedorId}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Seleccione...</option>
                    {proveedores.map(p => (
                      <option key={p.id} value={p.id}>{p.name}</option>
                    ))}
                  </select>
                </div>

                <div className="form-group">
                  <label className="form-label">Bodega *</label>
                  <select
                    name="bodegaId"
                    className="form-select"
                    value={formData.bodegaId}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Seleccione...</option>
                    {bodegas.map(b => (
                      <option key={b.id} value={b.id}>{b.name}</option>
                    ))}
                  </select>
                </div>
              </div>

              <div className="grid grid-cols-2">
                <div className="form-group">
                  <label className="form-label">Fecha Esperada</label>
                  <input
                    type="date"
                    name="fechaEsperada"
                    className="form-input"
                    value={formData.fechaEsperada}
                    onChange={handleChange}
                  />
                </div>

                <div className="form-group">
                  <label className="form-label">Notas</label>
                  <input
                    type="text"
                    name="notas"
                    className="form-input"
                    value={formData.notas}
                    onChange={handleChange}
                  />
                </div>
              </div>

              <h3 style={{ marginTop: '1rem', marginBottom: '0.5rem' }}>Detalles de la Orden</h3>
              
              {detalles.map((detalle, index) => (
                <div key={index} style={{ 
                  border: '1px solid var(--gray-200)', 
                  padding: '1rem', 
                  borderRadius: '6px', 
                  marginBottom: '0.5rem' 
                }}>
                  <div className="grid grid-cols-3">
                    <div className="form-group">
                      <label className="form-label">Producto *</label>
                      <select
                        className="form-select"
                        value={detalle.productoId}
                        onChange={(e) => handleDetalleChange(index, 'productoId', e.target.value)}
                        required
                      >
                        <option value="">Seleccione...</option>
                        {productos.map(p => (
                          <option key={p.id} value={p.id}>{p.name} ({p.sku})</option>
                        ))}
                      </select>
                    </div>

                    <div className="form-group">
                      <label className="form-label">Cantidad *</label>
                      <input
                        type="number"
                        className="form-input"
                        value={detalle.cantidad}
                        onChange={(e) => handleDetalleChange(index, 'cantidad', e.target.value)}
                        min="1"
                        required
                      />
                    </div>

                    <div className="form-group">
                      <label className="form-label">Precio Unit. *</label>
                      <input
                        type="number"
                        className="form-input"
                        value={detalle.precioUnitario}
                        onChange={(e) => handleDetalleChange(index, 'precioUnitario', e.target.value)}
                        step="0.01"
                        min="0.01"
                        required
                      />
                    </div>
                  </div>
                  
                  {detalles.length > 1 && (
                    <button
                      type="button"
                      className="btn btn-sm btn-danger"
                      onClick={() => eliminarDetalle(index)}
                    >
                      <Trash2 size={14} />
                      Eliminar
                    </button>
                  )}
                </div>
              ))}

              <button
                type="button"
                className="btn btn-secondary"
                onClick={agregarDetalle}
                style={{ marginBottom: '1rem' }}
              >
                <Plus size={18} />
                Agregar Producto
              </button>

              <div style={{ 
                backgroundColor: 'var(--gray-50)', 
                padding: '1rem', 
                borderRadius: '6px',
                marginBottom: '1rem'
              }}>
                <strong>Total: ${calcularTotal().toFixed(2)}</strong>
              </div>

              <div className="modal-footer">
                <button 
                  type="button" 
                  className="btn btn-secondary"
                  onClick={handleCloseModal}
                >
                  Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  Crear Orden
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}