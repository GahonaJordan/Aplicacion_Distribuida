package com.example.inventario_service.repositories;

import com.example.inventario_service.entities.Bodega;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BodegaRepository extends CrudRepository<Bodega, Long> {
    Optional<Bodega> findByCode(String code);
    boolean existsByCode(String code);

    // Reemplazado findByActiveTrue() por findByStatus(...)
    List<Bodega> findByStatus(Bodega.ProductStatus status);
}
