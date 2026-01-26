package com.example.inventario_service.repositories;

import com.example.inventario_service.entities.Inventario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends CrudRepository<Inventario, Long> {
    Optional<Inventario> findByProductIdAndBodegaId_Id(Long productId, Long bodegaId);
    boolean existsByProductIdAndBodegaId_Id(Long productId, Long bodegaId);
    List<Inventario> findByBodegaId_Id(Long bodegaId);
    List<Inventario> findByProductId(Long productId);
}
