package com.example.proveedor_service.repositories;

import com.example.proveedor_service.entities.Proveedor;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProveedorRepository extends CrudRepository<Proveedor, Long> {
    boolean existsByTaxId(String taxId);
    boolean existsByEmail(String email);
    Optional<Proveedor> findByTaxId(String taxId);
}
