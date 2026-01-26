## 🔐 Implementación de Roles y Control de Acceso

### 📋 Resumen de la Implementación

Se ha implementado un sistema completo de roles basado en Spring Security para controlar el acceso a los recursos del sistema. Los cambios incluyen:

---

## ✅ Cambios Realizados

### 1. **Entidades de Base de Datos**

#### Creadas:
- **Role.java** - Nueva entidad para almacenar roles
  - `id` (Long) - Identificador único
  - `name` (String) - Nombre del rol (ej: ROLE_ADMIN, ROLE_USER)
  - `description` (String) - Descripción del rol
  - Relación ManyToMany con User

- **RoleRepository.java** - Interfaz de acceso a datos para roles
  - Métodos: `findByName()`, `existsByName()`

#### Modificadas:
- **User.java** - Actualizada para soportar múltiples roles
  - Nuevo campo: `Set<Role> roles` con relación ManyToMany
  - Relación configurada con fetch EAGER para cargar roles automáticamente

### 2. **Servicios (OAuth Server)**

#### UserService.java
- **Cambio Principal:** Al registrar un usuario, se asigna automáticamente el rol `ROLE_USER`
- **Método modificado:** `registerUser()` ahora:
  1. Busca el rol ROLE_USER en la base de datos
  2. Asigna el rol al nuevo usuario
  3. Guarda el usuario con sus roles

#### SecurityConfig.java (OAuth Server)
- **UserDetailsService actualizado:** Ahora carga los roles del usuario desde la base de datos
- **Funcionamiento:** Convierte los roles de la entidad User a authorities de Spring Security
- Los roles se cargan de forma EAGER desde la BD

### 3. **Controladores**

#### AuthController.java (OAuth Server)
- **Login response:** Ahora retorna los roles del usuario
- **Register response:** Ahora retorna los roles asignados
- **DTOs actualizados:** UserResponse incluye campo `roles`

#### Microservicios - Control de Acceso con @PreAuthorize

**ProductoController.java:**
- `GET /api/productos` - ✅ ADMIN, USER (lectura)
- `POST /api/productos` - ✅ ADMIN (creación)
- `PUT /api/productos/{id}` - ✅ ADMIN (actualización)
- `DELETE /api/productos/{id}` - ✅ ADMIN (eliminación)

**ProveedorController.java:**
- `GET /api/proveedores` - ✅ ADMIN, USER (lectura)
- `POST /api/proveedores` - ✅ ADMIN (creación)
- `PUT /api/proveedores/{id}` - ✅ ADMIN (actualización)
- `DELETE /api/proveedores/{id}` - ✅ ADMIN (eliminación)

**OrdenCompraController.java:**
- `GET /api/ordenes-compra` - ✅ ADMIN, USER (lectura)
- `POST /api/ordenes-compra` - ✅ ADMIN, USER (creación de órdenes)
- `PATCH /{id}/aprobar` - ✅ ADMIN (aprobación)
- `PATCH /{id}/recibir` - ✅ ADMIN (recepción)
- `PATCH /{id}/cancelar` - ✅ ADMIN (cancelación)

**InventarioController.java:**
- `GET /api/inventarios` - ✅ ADMIN, USER (lectura)
- `POST /api/inventarios/.../movimientos` - ✅ ADMIN, USER (registrar movimientos)
- `GET /api/inventarios/.../movimientos` - ✅ ADMIN, USER (ver movimientos)

**BodegaController.java:**
- `GET /api/bodegas` - ✅ ADMIN, USER (lectura)
- `POST /api/bodegas` - ✅ ADMIN (creación)
- `PUT /api/bodegas/{id}` - ✅ ADMIN (actualización)
- `DELETE /api/bodegas/{id}` - ✅ ADMIN (eliminación)

### 4. **Configuración de Seguridad**

#### SecurityConfig de todos los microservicios
- ✅ Agregada anotación `@EnableMethodSecurity`
- Permite usar `@PreAuthorize` en los métodos de los controladores

#### Gateway (api-gateway)
- ✅ Creado filtro personalizado `RolesPropagationFilter`
- Propaga roles del JWT a través de headers HTTP:
  - `X-User-Roles` - Lista de roles separados por coma
  - `X-User-Name` - Username del usuario autenticado

### 5. **Datos Iniciales**

#### mysql-init/01-init.sql
Se han agregado inserciones de datos iniciales:

**Roles:**
```sql
INSERT IGNORE INTO roles (id, name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador con acceso completo'),
(2, 'ROLE_USER', 'Usuario con acceso limitado');
```

**Usuarios de prueba:**
1. **admin** 
   - Username: `admin`
   - Email: `admin@sistema.com`
   - Password: `admin123`
   - Roles: ROLE_ADMIN, ROLE_USER
   - ✅ Puede gestionar todo (productos, proveedores, órdenes, inventario)

2. **usuario** 
   - Username: `usuario`
   - Email: `usuario@sistema.com`
   - Password: `user123`
   - Roles: ROLE_USER
   - ✅ Puede ver información y crear órdenes
   - ❌ No puede eliminar datos

---

## 🔑 Flujo de Autenticación y Autorización

```
1. Usuario Login (OAuth Server)
   ↓
2. Validación de credenciales
   ↓
3. Carga de roles de BD
   ↓
4. Generación de JWT con roles
   ↓
5. Cliente envía JWT a API Gateway
   ↓
6. Gateway valida JWT y extrae roles
   ↓
7. Gateway propaga roles a microservicio
   ↓
8. Microservicio valida @PreAuthorize
   ↓
9. ✅ Permite/❌ Rechaza acceso
```

---

## 📝 Resumen de Permisos

### ROLE_ADMIN
| Operación | Productos | Proveedores | Órdenes | Inventario |
|-----------|-----------|-------------|---------|-----------|
| Ver (GET) | ✅ | ✅ | ✅ | ✅ |
| Crear (POST) | ✅ | ✅ | ✅ | ✅ |
| Editar (PUT) | ✅ | ✅ | ✅ | ✅ |
| Eliminar (DELETE) | ✅ | ✅ | ✅ | ✅ |
| Aprobar Órdenes | - | - | ✅ | - |
| Recibir Órdenes | - | - | ✅ | - |
| Cancelar Órdenes | - | - | ✅ | - |

### ROLE_USER
| Operación | Productos | Proveedores | Órdenes | Inventario |
|-----------|-----------|-------------|---------|-----------|
| Ver (GET) | ✅ | ✅ | ✅ | ✅ |
| Crear (POST) | ❌ | ❌ | ✅ | ✅* |
| Editar (PUT) | ❌ | ❌ | ❌ | ❌ |
| Eliminar (DELETE) | ❌ | ❌ | ❌ | ❌ |
| Registrar Movimientos | - | - | - | ✅ |

*Solo movimientos de inventario (entradas/salidas)

---

## 🧪 Pruebas

### Credenciales para pruebas:

**Administrador:**
```
POST /auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

**Usuario Normal:**
```
POST /auth/login
{
  "username": "usuario",
  "password": "user123"
}
```

### Respuesta esperada:
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "id": 1,
    "username": "admin",
    "email": "admin@sistema.com",
    "roles": ["ROLE_ADMIN", "ROLE_USER"]
  }
}
```

---

## 📌 Archivos Modificados

1. ✅ `oauth-server/src/main/java/com/espe/oauth_server/entity/Role.java` - CREADO
2. ✅ `oauth-server/src/main/java/com/espe/oauth_server/entity/User.java` - MODIFICADO
3. ✅ `oauth-server/src/main/java/com/espe/oauth_server/repository/RoleRepository.java` - CREADO
4. ✅ `oauth-server/src/main/java/com/espe/oauth_server/service/UserService.java` - MODIFICADO
5. ✅ `oauth-server/src/main/java/com/espe/oauth_server/config/SecurityConfig.java` - MODIFICADO
6. ✅ `oauth-server/src/main/java/com/espe/oauth_server/controller/AuthController.java` - MODIFICADO
7. ✅ `producto-service/src/main/java/.../controllers/ProductoController.java` - MODIFICADO
8. ✅ `proveedor-service/src/main/java/.../controllers/ProveedorController.java` - MODIFICADO
9. ✅ `inventario-service/src/main/java/.../controllers/InventarioController.java` - MODIFICADO
10. ✅ `inventario-service/src/main/java/.../controllers/BodegaController.java` - MODIFICADO
11. ✅ `ordenes-service/src/main/java/.../controller/OrdenCompraController.java` - MODIFICADO
12. ✅ `producto-service/src/main/java/.../config/SecurityConfig.java` - MODIFICADO
13. ✅ `inventario-service/src/main/java/.../config/SecurityConfig.java` - MODIFICADO
14. ✅ `proveedor-service/src/main/java/.../config/SecurityConfig.java` - MODIFICADO
15. ✅ `ordenes-service/src/main/java/.../config/SecurityConfig.java` - MODIFICADO
16. ✅ `gatway/src/main/java/.../SecurityConfig.java` - YA EXISTÍA
17. ✅ `gatway/src/main/java/.../RolesPropagationFilter.java` - CREADO
18. ✅ `mysql-init/01-init.sql` - MODIFICADO (agregados roles y usuarios de prueba)

---

## 🚀 Próximos Pasos (Opcional)

1. **Integración con Frontend:** Actualizar el React para mostrar/ocultar opciones según roles
2. **Auditoría:** Agregar logging de quién hizo qué
3. **Permisos Granulares:** Crear más roles específicos si es necesario
4. **Refresh Tokens:** Implementar rotación de tokens JWT
5. **Rate Limiting:** Considerar agregar límite de peticiones por rol

---

## ℹ️ Notas Importantes

- Los roles se cargan **automáticamente** al registrar un usuario (siempre ROLE_USER)
- Para convertir a ADMIN, debe actualizarse manualmente la BD
- Las anotaciones `@PreAuthorize` se validan en **tiempo de ejecución**
- El filtro del Gateway propaga roles para referencias posteriores en microservicios
- Los cambios son **retrocompatibles** con la configuración existente
