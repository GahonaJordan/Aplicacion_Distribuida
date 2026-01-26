-- ==========================
-- DATOS INICIALES PARA OAUTH SERVER
-- ==========================
-- Este script se ejecuta automáticamente por Spring Boot después de que Hibernate cree las tablas
-- spring.sql.init.mode=always en application.properties

-- Insertar roles si no existen
INSERT INTO roles (id, name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador con acceso completo'),
(2, 'ROLE_USER', 'Usuario con acceso limitado')
ON CONFLICT (id) DO NOTHING;

-- Insertar usuario admin
-- Credenciales: admin / admin123
-- Password almacenada en texto plano solo para desarrollo ({noop})
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
(1, 'admin', 'admin@sistema.com', '{noop}admin123', 'Admin', 'Sistema', true, true, true, true)
ON CONFLICT (id) DO NOTHING;

-- Insertar usuario de prueba
-- Credenciales: usuario / user123
-- Password almacenada en texto plano solo para desarrollo ({noop})
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
(2, 'usuario', 'usuario@sistema.com', '{noop}user123', 'Usuario', 'Prueba', true, true, true, true)
ON CONFLICT (id) DO NOTHING;

-- Asignar roles a usuarios
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin tiene rol ROLE_ADMIN
(1, 2), -- admin también tiene rol ROLE_USER
(2, 2)  -- usuario tiene solo rol ROLE_USER
ON CONFLICT DO NOTHING;

-- Ajustar las secuencias para que los IDs autogenerados no colisionen
SELECT setval(pg_get_serial_sequence('roles', 'id'), (SELECT COALESCE(MAX(id), 0) FROM roles));
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT COALESCE(MAX(id), 0) FROM users));
