-- Crear bases de datos necesarias
CREATE DATABASE IF NOT EXISTS oauth_server_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS producto_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS proveedor_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS inventario_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ordenes_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario y otorgar permisos
CREATE USER IF NOT EXISTS 'approot'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON oauth_server_db.* TO 'approot'@'%';
GRANT ALL PRIVILEGES ON producto_service_db.* TO 'approot'@'%';
GRANT ALL PRIVILEGES ON proveedor_service_db.* TO 'approot'@'%';
GRANT ALL PRIVILEGES ON inventario_service_db.* TO 'approot'@'%';
GRANT ALL PRIVILEGES ON ordenes_service_db.* TO 'approot'@'%';
FLUSH PRIVILEGES;

-- ==========================
-- INSERTAR DATOS INICIALES PARA OAUTH SERVER
-- ==========================
-- NOTA: Las tablas se crean automáticamente con Hibernate (spring.jpa.hibernate.ddl-auto=update)
-- Las inserciones iniciales se harán después mediante un script separado
-- o manualmente a través de la API de registro cuando todas las tablas estén creadas.

