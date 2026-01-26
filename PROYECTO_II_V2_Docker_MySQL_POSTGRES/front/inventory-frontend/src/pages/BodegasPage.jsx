import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, X, Package } from 'lucide-react';
import { bodegaService } from '../services/bodegaService';
import { inventarioService } from '../services/inventarioService';
import { useNavigate } from 'react-router-dom';

export default function BodegasPage() {
  const [bodegas, setBodegas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingBodega, setEditingBodega] = useState(null);
  const [alert, setAlert] = useState(null);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    code: '',
    name: '',
    address: '',
    city: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    loadBodegas();
  }, []);

  const loadBodegas = async () => {
    try {
      setLoading(true);
      const data = await bodegaService.getAll();
      setBodegas(data);
    } catch (error) {
      showAlert('Error al cargar bodegas: ' + error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, type = 'success') => {
    setAlert({ message, type });
    setTimeout(() => setAlert(null), 5000);
  };

  const handleOpenModal = (bodega = null) => {
    if (bodega) {
      setEditingBodega(bodega);
      setFormData({
        code: bodega.code,
        name: bodega.name,
        address: bodega.address || '',
        city: bodega.city || '',
        status: bodega.status || 'ACTIVE'
      });
    } else {
      setEditingBodega(null);
      setFormData({
        code: '',
        name: '',
        address: '',
        city: '',
        status: 'ACTIVE'
      });
    }
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditingBodega(null);
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
    
    // Validación en el cliente
    if (!formData.code || !formData.name || !formData.status) {
      showAlert('Código, Nombre y Estado son obligatorios', 'error');
      return;
    }
    
    if (!formData.code.match(/^[A-Z0-9-]+$/)) {
      showAlert('Código solo puede contener mayúsculas, números y guiones', 'error');
      return;
    }
    
    try {
      console.log('Datos a enviar (bodega):', {
        code: formData.code,
        name: formData.name,
        address: formData.address,
        city: formData.city,
        status: formData.status
      });
      
      if (editingBodega) {
        const response = await bodegaService.update(editingBodega.id, formData);
        console.log('Bodega actualizada:', response);
        showAlert('Bodega actualizada exitosamente');
      } else {
        const response = await bodegaService.create(formData);
        console.log('Bodega creada:', response);
        showAlert('Bodega creada exitosamente');
      }

      handleCloseModal();
      loadBodegas();
    } catch (error) {
      console.error('Error completo:', error);
      console.error('Respuesta del servidor:', error.response?.data);
      console.error('Estado HTTP:', error.response?.status);
      
      const errorMsg = error.response?.data?.message 
        || error.response?.data?.error
        || error.response?.data?.details
        || error.message;
      
      showAlert('Error: ' + errorMsg, 'error');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('¿Está seguro de eliminar esta bodega?')) return;

    try {
      await bodegaService.delete(id);
      showAlert('Bodega eliminada exitosamente');
      loadBodegas();
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
          <h1 className="card-title">Gestión de Bodegas</h1>
          <button className="btn btn-primary" onClick={() => handleOpenModal()}>
            <Plus size={18} />
            Nueva Bodega
          </button>
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Código</th>
                <th>Nombre</th>
                <th>Ciudad</th>
                <th>Dirección</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {bodegas.length === 0 ? (
                <tr>
                  <td colSpan="6" style={{ textAlign: 'center', padding: '2rem' }}>
                    No hay bodegas registradas
                  </td>
                </tr>
              ) : (
                bodegas.map(bodega => (
                  <tr key={bodega.id}>
                    <td><strong>{bodega.code}</strong></td>
                    <td>{bodega.name}</td>
                    <td>{bodega.city || '-'}</td>
                    <td>{bodega.address || '-'}</td>
                    <td>
                      <span className={`badge ${bodega.status === 'ACTIVE' ? 'badge-success' : 'badge-danger'}`}>
                        {bodega.status === 'ACTIVE' ? 'Activa' : 'Inactiva'}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        <button 
                          className="btn btn-sm btn-secondary"
                          onClick={() => handleOpenModal(bodega)}
                          title="Editar"
                        >
                          <Edit size={16} />
                        </button>
                        <button 
                          className="btn btn-sm btn-danger"
                          onClick={() => handleDelete(bodega.id)}
                          title="Eliminar"
                        >
                          <Trash2 size={16} />
                        </button>
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
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2 className="modal-title">
                {editingBodega ? 'Editar Bodega' : 'Nueva Bodega'}
              </h2>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Código * (Solo mayúsculas, números y guiones)</label>
                <input
                  type="text"
                  name="code"
                  className="form-input"
                  value={formData.code}
                  onChange={handleChange}
                  disabled={editingBodega !== null}
                  maxLength="50"
                  placeholder="BOD-001"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Nombre * (Máx. 200 caracteres)</label>
                <input
                  type="text"
                  name="name"
                  className="form-input"
                  value={formData.name}
                  onChange={handleChange}
                  maxLength="200"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Ciudad (Máx. 50 caracteres)</label>
                <input
                  type="text"
                  name="city"
                  className="form-input"
                  value={formData.city}
                  onChange={handleChange}
                  maxLength="50"
                />
              </div>

              <div className="form-group">
                <label className="form-label">Dirección (Máx. 500 caracteres)</label>
                <textarea
                  name="address"
                  className="form-textarea"
                  value={formData.address}
                  onChange={handleChange}
                  maxLength="500"
                  rows="3"
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
                  <option value="ACTIVE">Activa</option>
                  <option value="INACTIVE">Inactiva</option>
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
                  {editingBodega ? 'Actualizar' : 'Crear'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}