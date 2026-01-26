# 👤 CREDENCIALES DEL SISTEMA

## Super Administrador
El sistema viene con un usuario administrador preconfigurado:

- **Usuario:** `admin`
- **Contraseña:** `admin123`
- **Email:** admin@sistema.com
- **Roles:** ROLE_ADMIN, ROLE_USER

Este usuario tiene acceso completo al sistema incluyendo:
- ✅ Gestión de productos
- ✅ Gestión de proveedores
- ✅ Gestión de bodegas
- ✅ Gestión de inventario
- ✅ Gestión de órdenes
- ✅ **Gestión de usuarios y roles** (Exclusivo para admins)

## Usuario de Prueba
También existe un usuario regular de prueba:

- **Usuario:** `usuario`
- **Contraseña:** `user123`
- **Email:** usuario@sistema.com
- **Roles:** ROLE_USER

Este usuario tiene acceso limitado:
- ✅ Ver productos (solo lectura)
- ✅ Ver proveedores (solo lectura)
- ✅ Ver inventario (solo lectura)
- ✅ Gestionar órdenes
- ❌ No puede crear/editar/eliminar productos ni proveedores
- ❌ No puede acceder a bodegas
- ❌ No puede gestionar usuarios

## 🎯 Panel de Gestión de Usuarios

El super administrador puede:
1. Ver todos los usuarios registrados en el sistema
2. Asignar o remover roles (ROLE_ADMIN, ROLE_USER)
3. Eliminar usuarios (excepto el admin principal)
4. Ver información detallada de cada usuario

### ¿Cómo funciona?
1. Los usuarios se registran desde la interfaz → reciben automáticamente `ROLE_USER`
2. El administrador ingresa con la cuenta `admin`
3. Va al menú "Usuarios" (solo visible para admins)
4. Puede cambiar los roles de cualquier usuario registrado
5. Al asignar `ROLE_ADMIN`, el usuario obtiene acceso completo

## 🔐 Seguridad
- Las contraseñas están encriptadas con BCrypt
- Los endpoints de gestión de usuarios requieren rol ROLE_ADMIN
- El usuario admin principal no puede ser eliminado
- Un usuario no puede eliminarse a sí mismo
