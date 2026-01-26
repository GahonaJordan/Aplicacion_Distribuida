import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, X } from 'lucide-react';
import { productoService } from '../services/productoService';
import { useAuth } from '../context/AuthContext';
import { hasAdminAccess } from '../utils/roleUtils';

export default function ProductosPage() {
  const { user } = useAuth();
  const userHasAdminAccess = hasAdminAccess(user);
  const [productos, setProductos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProducto, setEditingProducto] = useState(null);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    sku: '',
    name: '',
    description: '',
    price: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    loadProductos();
  }, []);

  const loadProductos = async () => {
    try {
      setLoading(true);
      const data = await productoService.getAll();
      setProductos(data);
    } catch (error) {
      showAlert('Error al cargar productos: ' + error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, type = 'success') => {
    setAlert({ message, type });
    setTimeout(() => setAlert(null), 5000);
  };

  const handleOpenModal = (producto = null) => {
    if (producto) {
      setEditingProducto(producto);
      setFormData({
        sku: producto.sku,
        name: producto.name,
        description: producto.description || '',
        price: producto.price,
        status: producto.status || 'ACTIVE'
      });
    } else {
      setEditingProducto(null);
      setFormData({
        sku: '',
        name: '',
        description: '',
        price: '',
        status: 'ACTIVE'
      });
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingProducto(null);
    setFormData({
      sku: '',
      name: '',
      description: '',
      price: '',
      status: 'ACTIVE'
    });
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const dataToSend = {
        ...formData,
        price: parseFloat(formData.price)
      };

      if (editingProducto) {
        await productoService.update(editingProducto.id, dataToSend);
        showAlert('Producto actualizado exitosamente');
      } else {
        await productoService.create(dataToSend);
        showAlert('Producto creado exitosamente');
      }

      handleCloseModal();
      loadProductos();
    } catch (error) {
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este producto?')) return;

    try {
      await productoService.delete(id);
      showAlert('Producto eliminado exitosamente');
      loadProductos();
    } catch (error) {
      showAlert('Error al eliminar: ' + error.message, 'error');
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
      {alert && (
        <div className={`alert alert-${alert.type}`}>
          {alert.message}
        </div>
      )}

      <div className="card">
        <div className="card-header">
          <h1 className="card-title">Gestión de Productos</h1>
          {userHasAdminAccess && (
            <button className="btn btn-primary" onClick={() => handleOpenModal()}>
              <Plus size={18} />
              Nuevo Producto
            </button>
          )}
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>SKU</th>
                <th>Nombre</th>
                <th>Precio</th>
                <th>Estado</th>
                {userHasAdminAccess && <th>Acciones</th>}
              </tr>
            </thead>
            <tbody>
              {productos.length === 0 ? (
                <tr>
                  <td colSpan={userHasAdminAccess ? "5" : "4"} style={{ textAlign: 'center', padding: '2rem' }}>
                    No hay productos registrados
                  </td>
                </tr>
              ) : (
                productos.map(producto => (
                  <tr key={producto.id}>
                    <td><strong>{producto.sku}</strong></td>
                    <td>{producto.name}</td>
                    <td>${parseFloat(producto.price).toFixed(2)}</td>
                    <td>
                      <span className={`badge ${producto.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}`}>
                        {producto.status === 'ACTIVE' ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    {userHasAdminAccess && (
                      <td>
                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                          <button 
                            className="btn btn-sm btn-secondary"
                            onClick={() => handleOpenModal(producto)}
                          >
                            <Edit size={16} />
                          </button>
                          <button 
                            className="btn btn-sm btn-danger"
                            onClick={() => handleDelete(producto.id)}
                          >
                            <Trash2 size={16} />
                          </button>
                        </div>
                      </td>
                    )}
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
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">
                {editingProducto ? 'Editar Producto' : 'Nuevo Producto'}
              </h2>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">SKU *</label>
                <input
                  type="text"
                  name="sku"
                  className="form-input"
                  value={formData.sku}
                  onChange={handleChange}
                  disabled={editingProducto !== null}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Nombre *</label>
                <input
                  type="text"
                  name="name"
                  className="form-input"
                  value={formData.name}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Descripción</label>
                <textarea
                  name="description"
                  className="form-textarea"
                  value={formData.description}
                  onChange={handleChange}
                  rows="3"
                />
              </div>

              <div className="form-group">
                <label className="form-label">Precio *</label>
                <input
                  type="number"
                  name="price"
                  className="form-input"
                  value={formData.price}
                  onChange={handleChange}
                  step="0.01"
                  min="0.01"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Estado *</label>
                <select
                  name="status"
                  className="form-input"
                  value={formData.status}
                  onChange={handleChange}
                  required
                >
                  <option value="ACTIVE">Activo</option>
                  <option value="INACTIVE">Inactivo</option>
                </select>
              </div>

              <div className="modal-footer">
                <button 
                  type="button" 
                  className="btn btn-secondary"
                  onClick={handleCloseModal}
                >
                  <X size={18} />
                  Cancelar
                </button>
                <button type="submit" className="btn btn-primary">
                  {editingProducto ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}