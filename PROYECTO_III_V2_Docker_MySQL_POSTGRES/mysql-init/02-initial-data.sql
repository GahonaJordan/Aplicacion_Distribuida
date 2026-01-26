-- ==========================
-- INSERTAR DATOS INICIALES - ROLES Y USUARIOS
-- ==========================
-- Este script se ejecuta después que Hibernate crea las tablas
-- Esperar a que oauth_server_db esté lista y las tablas creadas

USE oauth_server_db;

-- Insertar roles si no existen
INSERT IGNORE INTO roles (id, name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador con acceso completo'),
(2, 'ROLE_USER', 'Usuario con acceso limitado');

-- Limpiar relaciones existentes para reinicializar
DELETE FROM user_roles WHERE user_id IN (1, 2);

-- Limpiar usuarios existentes
DELETE FROM users WHERE id IN (1, 2);

-- Insertar usuario admin
-- Credenciales: admin / admin123
-- Password BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
(1, 'admin', 'admin@sistema.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin', 'Sistema', true, true, true, true);

-- Insertar usuario de prueba
-- Credenciales: usuario / user123
-- Password BCrypt hash: $2a$10$fHnqZQUr.0.cOJr0jWzZ7eR3HWv7v0FqhK5VBqZO7G5VjXG8.8FHy
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
(2, 'usuario', 'usuario@sistema.com', '$2a$10$fHnqZQUr.0.cOJr0jWzZ7eR3HWv7v0FqhK5VBqZO7G5VjXG8.8FHy', 'Usuario', 'Prueba', true, true, true, true);

-- Asignar roles a usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin tiene rol ROLE_ADMIN
(1, 2), -- admin también tiene rol ROLE_USER
(2, 2); -- usuario tiene solo rol ROLE_USER
