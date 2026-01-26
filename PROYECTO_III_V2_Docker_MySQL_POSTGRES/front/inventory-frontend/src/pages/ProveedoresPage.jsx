import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, X } from 'lucide-react';
import { proveedorService } from '../services/proveedorService';
import { useAuth } from '../context/AuthContext';
import { hasAdminAccess } from '../utils/roleUtils';

export default function ProveedoresPage() {
  const { user } = useAuth();
  const userHasAdminAccess = hasAdminAccess(user);
  const [proveedores, setProveedores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingProveedor, setEditingProveedor] = useState(null);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    taxId: '',
    email: '',
    phone: '',
    address: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    loadProveedores();
  }, []);

  const loadProveedores = async () => {
    try {
      setLoading(true);
      const data = await proveedorService.getAll();
      setProveedores(data);
    } catch (error) {
      showAlert('Error al cargar proveedores: ' + error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, type = 'success') => {
    setAlert({ message, type });
    setTimeout(() => setAlert(null), 5000);
  };

  const handleOpenModal = (proveedor = null) => {
    if (proveedor) {
      setEditingProveedor(proveedor);
      setFormData({
        name: proveedor.name,
        taxId: proveedor.taxId,
        email: proveedor.email,
        phone: proveedor.phone,
        address: proveedor.address || '',
        status: proveedor.status || 'ACTIVE'
      });
    } else {
      setEditingProveedor(null);
      setFormData({
        name: '',
        taxId: '',
        email: '',
        phone: '',
        address: '',
        status: 'ACTIVE'
      });
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingProveedor(null);
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === 'phone') {
      const numeric = value.replace(/\D/g, '').slice(0, 10);
      setFormData(prev => ({
        ...prev,
        [name]: numeric
      }));
      return;
    }
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (formData.phone.length !== 10) {
      showAlert('El teléfono debe tener exactamente 10 dígitos', 'error');
      return;
    }
    
    try {
      console.log('Datos a enviar:', formData);
      
      if (editingProveedor) {
        await proveedorService.update(editingProveedor.id, formData);
        showAlert('Proveedor actualizado exitosamente');
      } else {
        await proveedorService.create(formData);
        showAlert('Proveedor creado exitosamente');
      }

      handleCloseModal();
      loadProveedores();
    } catch (error) {
      console.error('Error completo:', error);
      console.error('Respuesta del servidor:', error.response?.data);
      showAlert('Error: ' + (error.response?.data?.message || error.message), 'error');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar este proveedor?')) return;

    try {
      await proveedorService.delete(id);
      showAlert('Proveedor eliminado exitosamente');
      loadProveedores();
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
          <h1 className="card-title">Gestión de Proveedores</h1>
          {userHasAdminAccess && (
            <button className="btn btn-primary" onClick={() => handleOpenModal()}>
              <Plus size={18} />
              Nuevo Proveedor
            </button>
          )}
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>RUC/NIT</th>
                <th>Email</th>
                <th>Teléfono</th>
                <th>Dirección</th>
                <th>Estado</th>
                {userHasAdminAccess && <th>Acciones</th>}
              </tr>
            </thead>
            <tbody>
              {proveedores.length === 0 ? (
                <tr>
                  <td colSpan={userHasAdminAccess ? "7" : "6"} style={{ textAlign: 'center', padding: '2rem' }}>
                    No hay proveedores registrados
                  </td>
                </tr>
              ) : (
                proveedores.map(proveedor => (
                  <tr key={proveedor.id}>
                    <td><strong>{proveedor.name}</strong></td>
                    <td>{proveedor.taxId}</td>
                    <td>{proveedor.email}</td>
                    <td>{proveedor.phone}</td>
                    <td>{proveedor.address || '-'}</td>
                    <td>
                      <span className={`badge ${proveedor.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}`}>
                        {proveedor.status === 'ACTIVE' ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    {userHasAdminAccess && (
                      <td>
                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                          <button 
                            className="btn btn-sm btn-secondary"
                            onClick={() => handleOpenModal(proveedor)}
                          >
                            <Edit size={16} />
                          </button>
                          <button 
                            className="btn btn-sm btn-danger"
                            onClick={() => handleDelete(proveedor.id)}
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
                {editingProveedor ? 'Editar Proveedor' : 'Nuevo Proveedor'}
              </h2>
            </div>

            <form onSubmit={handleSubmit}>
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
                <label className="form-label">RUC/NIT *</label>
                <input
                  type="text"
                  name="taxId"
                  className="form-input"
                  value={formData.taxId}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="grid grid-cols-2">
                <div className="form-group">
                  <label className="form-label">Email *</label>
                  <input
                    type="email"
                    name="email"
                    className="form-input"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="form-group">
                  <label className="form-label">Teléfono *</label>
                  <input
                    type="tel"
                    name="phone"
                    className="form-input"
                    value={formData.phone}
                    onChange={handleChange}
                    maxLength="10"
                    pattern="[0-9]{10}"
                    title="Ingrese 10 dígitos"
                    required
                  />
                  <small style={{ color: 'var(--gray-600)' }}>Solo dígitos, 10 caracteres.</small>
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Dirección *</label>
                <textarea
                  name="address"
                  className="form-textarea"
                  value={formData.address}
                  onChange={handleChange}
                  rows="3"
                  maxLength="500"
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
                  {editingProveedor ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}