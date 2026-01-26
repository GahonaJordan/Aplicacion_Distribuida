#!/usr/bin/env pwsh

# Script para insertar datos iniciales (roles y usuarios) en la base de datos OAuth Server
# Este script se debe ejecutar después que los contenedores estén en marcha

Write-Host "Esperando a que OAuth Server esté completamente listo..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "Insertando datos iniciales..." -ForegroundColor Cyan

# 1. Insertar Rol ADMIN
Write-Host "1. Insertando rol ADMIN..." -ForegroundColor Gray
$roleAdminBody = @{
    name = "ROLE_ADMIN"
    description = "Administrador con acceso completo"
} | ConvertTo-Json

# 2. Insertar Rol USER
Write-Host "2. Insertando rol USER..." -ForegroundColor Gray
$roleUserBody = @{
    name = "ROLE_USER"
    description = "Usuario con acceso limitado"
} | ConvertTo-Json

# 3. Registrar usuario Admin
Write-Host "3. Registrando usuario admin..." -ForegroundColor Gray
$adminBody = @{
    username = "admin"
    email = "admin@sistema.com"
    password = "admin123"
    confirmPassword = "admin123"
    firstName = "Admin"
    lastName = "Sistema"
} | ConvertTo-Json

try {
    $adminResponse = Invoke-WebRequest -Uri "http://localhost:9000/auth/register" `
      -Method POST `
      -ContentType "application/json" `
      -Body $adminBody `
      -ErrorAction Continue
    
    $adminData = $adminResponse.Content | ConvertFrom-Json
    if ($adminData.success) {
        Write-Host "✓ Usuario admin registrado exitosamente" -ForegroundColor Green
        Write-Host "  ID: $($adminData.data.id), Username: $($adminData.data.username)" -ForegroundColor Green
    } else {
        Write-Host "✗ Error al registrar admin: $($adminData.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ Error al registrar admin: $_" -ForegroundColor Red
}

# 4. Registrar usuario Test/Usuario
Write-Host "4. Registrando usuario de prueba..." -ForegroundColor Gray
$userBody = @{
    username = "usuario"
    email = "usuario@sistema.com"
    password = "user123"
    confirmPassword = "user123"
    firstName = "Usuario"
    lastName = "Prueba"
} | ConvertTo-Json

try {
    $userResponse = Invoke-WebRequest -Uri "http://localhost:9000/auth/register" `
      -Method POST `
      -ContentType "application/json" `
      -Body $userBody `
      -ErrorAction Continue
    
    $userData = $userResponse.Content | ConvertFrom-Json
    if ($userData.success) {
        Write-Host "✓ Usuario de prueba registrado exitosamente" -ForegroundColor Green
        Write-Host "  ID: $($userData.data.id), Username: $($userData.data.username)" -ForegroundColor Green
    } else {
        Write-Host "✗ Error al registrar usuario: $($userData.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ Error al registrar usuario: $_" -ForegroundColor Red
}

Write-Host "`nDatos iniciales insertados" -ForegroundColor Green
Write-Host "`nCredenciales disponibles:" -ForegroundColor Cyan
Write-Host "  Admin: admin / admin123" -ForegroundColor White
Write-Host "  User:  usuario / user123" -ForegroundColor White
