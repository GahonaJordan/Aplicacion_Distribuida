// src/utils/roleUtils.js

/**
 * Verifica si un usuario tiene un rol específico
 * @param {Object} user - El objeto del usuario
 * @param {string} role - El rol a verificar (ej: 'ROLE_ADMIN')
 * @returns {boolean}
 */
export const hasRole = (user, role) => {
  if (!user) return false;
  
  // Soporta tanto array de roles como una propiedad role simple
  if (user.roles && Array.isArray(user.roles)) {
    return user.roles.includes(role);
  }
  
  return user.role === role;
};

/**
 * Verifica si un usuario es administrador
 * @param {Object} user - El objeto del usuario
 * @returns {boolean}
 */
export const isAdmin = (user) => {
  return hasRole(user, 'ROLE_ADMIN');
};

/**
 * Verifica si un usuario tiene acceso de administrador
 * (es decir, NO es un usuario común)
 * @param {Object} user - El objeto del usuario
 * @returns {boolean}
 */
export const hasAdminAccess = (user) => {
  if (!user) return false;
  
  if (user.roles && Array.isArray(user.roles)) {
    return user.roles.some(role => role !== 'ROLE_USER');
  }
  
  return user.role && user.role !== 'ROLE_USER';
};

/**
 * Verifica si un usuario es un usuario común
 * @param {Object} user - El objeto del usuario
 * @returns {boolean}
 */
export const isRegularUser = (user) => {
  return hasRole(user, 'ROLE_USER');
};

/**
 * Obtiene todos los roles de un usuario como array
 * @param {Object} user - El objeto del usuario
 * @returns {Array}
 */
export const getUserRoles = (user) => {
  if (!user) return [];
  
  if (user.roles && Array.isArray(user.roles)) {
    return user.roles;
  }
  
  return user.role ? [user.role] : [];
};
