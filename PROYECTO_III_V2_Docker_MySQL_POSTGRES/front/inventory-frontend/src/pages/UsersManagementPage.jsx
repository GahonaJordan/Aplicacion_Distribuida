import { useState, useEffect } from 'react';
import { Users, Shield, UserCheck, Trash2, X, Check } from 'lucide-react';
import { userService } from '../services/userService';
import { useAuth } from '../context/AuthContext';
import { isAdmin } from '../utils/roleUtils';

export default function UsersManagementPage() {
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [alert, setAlert] = useState(null);
  const [editingUser, setEditingUser] = useState(null);
  const [selectedRoles, setSelectedRoles] = useState([]);

  const availableRoles = [
    { value: 'ROLE_USER', label: 'Usuario', icon: UserCheck, color: '#3b82f6' },
    { value: 'ROLE_ADMIN', label: 'Administrador', icon: Shield, color: '#ef4444' }
  ];

  useEffect(() => {
    // Solo admins pueden ver esta página
    if (!isAdmin(currentUser)) {
      setAlert({ message: 'No tienes permisos para acceder a esta página', type: 'error' });
      return;
    }
    loadUsers();
  }, [currentUser]);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const data = await userService.getAll();
      setUsers(data);
    } catch (error) {
      showAlert('Error al cargar usuarios: ' + error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  const showAlert = (message, type = 'success') => {
    setAlert({ message, type });
    setTimeout(() => setAlert(null), 5000);
  };

  const handleEditRoles = (user) => {
    if (user.username === 'admin') {
      showAlert('No se puede modificar el usuario administrador principal', 'error');
      return;
    }
    setEditingUser(user);
    setSelectedRoles(Array.from(user.roles));
  };

  const handleCancelEdit = () => {
    setEditingUser(null);
    setSelectedRoles([]);
  };

  const toggleRole = (role) => {
    // Comportamiento de radio button - solo un rol a la vez
    setSelectedRoles([role]);
  };

  const handleSaveRoles = async () => {
    if (selectedRoles.length !== 1) {
      showAlert('Debe seleccionar exactamente un rol', 'error');
      return;
    }

    try {
      await userService.updateRoles(editingUser.id, selectedRoles);
      showAlert('Rol actualizado exitosamente');
      handleCancelEdit();
      loadUsers();
    } catch (error) {
      showAlert('Error al actualizar rol: ' + error.message, 'error');
    }
  };

  const handleDeleteUser = async (userId, username) => {
    if (username === 'admin') {
      showAlert('No se puede eliminar el usuario administrador principal', 'error');
      return;
    }

    if (userId === currentUser?.id) {
      showAlert('No puedes eliminar tu propio usuario', 'error');
      return;
    }

    if (!window.confirm(`¿Está seguro de eliminar al usuario "${username}"?`)) {
      return;
    }

    try {
      await userService.delete(userId);
      showAlert('Usuario eliminado exitosamente');
      loadUsers();
    } catch (error) {
      showAlert('Error al eliminar usuario: ' + error.message, 'error');
    }
  };

  const getRoleBadgeColor = (role) => {
    if (role === 'ROLE_ADMIN') return 'badge-danger';
    return 'badge-primary';
  };

  const getRoleLabel = (role) => {
    const roleObj = availableRoles.find(r => r.value === role);
    return roleObj ? roleObj.label : role;
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  if (!isAdmin(currentUser)) {
    return (
      <div className="card">
        <div style={{ textAlign: 'center', padding: '2rem' }}>
          <h2>Acceso Denegado</h2>
          <p>No tienes permisos para acceder a esta página.</p>
        </div>
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
          <div>
            <h1 className="card-title">
              <Users size={24} style={{ display: 'inline', marginRight: '8px' }} />
              Gestión de Usuarios
            </h1>
            <p style={{ color: '#666', fontSize: '0.9rem', marginTop: '0.5rem' }}>
              Administra los roles y permisos de los usuarios del sistema
            </p>
          </div>
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Usuario</th>
                <th>Nombre Completo</th>
                <th>Email</th>
                <th>Roles</th>
                <th>Estado</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 ? (
                <tr>
                  <td colSpan="7" style={{ textAlign: 'center', padding: '2rem' }}>
                    No hay usuarios registrados
                  </td>
                </tr>
              ) : (
                users.map(user => (
                  <tr key={user.id}>
                    <td><strong>#{user.id}</strong></td>
                    <td>
                      <strong>{user.username}</strong>
                      {user.username === 'admin' && (
                        <span style={{ marginLeft: '8px', fontSize: '0.75rem', color: '#ef4444' }}>
                          (Principal)
                        </span>
                      )}
                    </td>
                    <td>{user.firstName} {user.lastName}</td>
                    <td>{user.email}</td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                        {Array.from(user.roles).map(role => (
                          <span key={role} className={`badge ${getRoleBadgeColor(role)}`}>
                            {getRoleLabel(role)}
                          </span>
                        ))}
                      </div>
                    </td>
                    <td>
                      <span className={`badge ${user.enabled ? 'badge-success' : 'badge-danger'}`}>
                        {user.enabled ? 'Activo' : 'Inactivo'}
                      </span>
                    </td>
                    <td>
                      <div style={{ display: 'flex', gap: '0.5rem' }}>
                        {user.username !== 'admin' && (
                          <button 
                            className="btn btn-sm btn-secondary"
                            onClick={() => handleEditRoles(user)}
                            title="Editar roles"
                          >
                            <Shield size={16} />
                          </button>
                        )}
                        {user.username !== 'admin' && user.id !== currentUser?.id && (
                          <button 
                            className="btn btn-sm btn-danger"
                            onClick={() => handleDeleteUser(user.id, user.username)}
                            title="Eliminar usuario"
                          >
                            <Trash2 size={16} />
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

      {/* Modal de edición de roles */}
      {editingUser && (
        <div className="modal-overlay" onClick={handleCancelEdit}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>
                <Shield size={20} style={{ display: 'inline', marginRight: '8px' }} />
                Editar Roles: {editingUser.username}
              </h2>
              <button onClick={handleCancelEdit} className="modal-close">
                <X size={20} />
              </button>
            </div>

            <div className="modal-body">
              <div style={{ marginBottom: '1rem' }}>
                <p style={{ color: '#666', fontSize: '0.9rem' }}>
                  Nombre: <strong>{editingUser.firstName} {editingUser.lastName}</strong>
                </p>
                <p style={{ color: '#666', fontSize: '0.9rem' }}>
                  Email: <strong>{editingUser.email}</strong>
                </p>
              </div>

              <div style={{ marginTop: '1.5rem' }}>
                <label style={{ display: 'block', marginBottom: '1rem', fontWeight: 'bold' }}>
                  Selecciona los roles del usuario:
                </label>
                
                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                  {availableRoles.map(role => {
                    const RoleIcon = role.icon;
                    const isSelected = selectedRoles.includes(role.value);
                    
                    return (
                      <div
                        key={role.value}
                        onClick={() => toggleRole(role.value)}
                        style={{
                          display: 'flex',
                          alignItems: 'center',
                          padding: '1rem',
                          border: `2px solid ${isSelected ? role.color : '#ddd'}`,
                          borderRadius: '8px',
                          cursor: 'pointer',
                          backgroundColor: isSelected ? `${role.color}10` : 'white',
                          transition: 'all 0.2s'
                        }}
                      >
                        <div style={{
                          width: '24px',
                          height: '24px',
                          borderRadius: '50%',
                          border: `2px solid ${isSelected ? role.color : '#ddd'}`,
                          backgroundColor: isSelected ? role.color : 'white',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                          marginRight: '1rem'
                        }}>
                          {isSelected && <Check size={16} color="white" />}
                        </div>
                        
                        <RoleIcon size={20} color={role.color} style={{ marginRight: '0.75rem' }} />
                        
                        <div style={{ flex: 1 }}>
                          <div style={{ fontWeight: 'bold', color: role.color }}>
                            {role.label}
                          </div>
                          <div style={{ fontSize: '0.85rem', color: '#666' }}>
                            {role.value === 'ROLE_ADMIN' 
                              ? 'Acceso completo al sistema y gestión de usuarios'
                              : 'Acceso limitado, solo consulta y órdenes'}
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {selectedRoles.length !== 1 && (
                <p style={{ color: '#ef4444', fontSize: '0.9rem', marginTop: '1rem' }}>
                  * Debes seleccionar exactamente un rol
                </p>
              )}
            </div>

            <div className="modal-footer">
              <button onClick={handleCancelEdit} className="btn btn-secondary">
                Cancelar
              </button>
              <button 
                onClick={handleSaveRoles} 
                className="btn btn-primary"
                disabled={selectedRoles.length !== 1}
              >
                <Check size={18} />
                Guardar Cambios
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
