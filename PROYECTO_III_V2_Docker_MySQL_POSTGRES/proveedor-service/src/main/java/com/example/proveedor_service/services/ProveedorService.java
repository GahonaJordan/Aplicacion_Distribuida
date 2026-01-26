package com.example.proveedor_service.services;

import com.example.proveedor_service.dto.ProveedorDto;
import com.example.proveedor_service.entities.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {

    // Listar todos los proveedores
    List<Proveedor> findAll();

    // Buscar un proveedor por id
    Optional<Proveedor> findById(Long id);

    // Crear un nuevo proveedor
    Proveedor create(ProveedorDto dto);

    // Actualizar un proveedor existente
    Proveedor update(Long id, ProveedorDto dto);

    // Eliminar un proveedor por id
    void deleteById(Long id);
}
