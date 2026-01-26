package com.example.inventario_service.repositories;

import com.example.inventario_service.entities.InventoryMovement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryMovementRepository extends CrudRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByBodegaId_Id(Long bodegaId);
    List<InventoryMovement> findByProductId(Long productId);
    List<InventoryMovement> findByBodegaId_IdAndProductId(Long bodegaId, Long productId);
}
