package com.example.inventario_service.services;

import com.example.inventario_service.dto.*;
import com.example.inventario_service.entities.Inventario;

import java.util.List;

public interface InventarioService {

    // Métodos legacy
    void addMovements(Long bodegaId, List<InventoryMovementDto> movements);

    List<Inventario> findByBodegaId(Long bodegaId);

    Inventario findByProductAndBodega(Long productId, Long bodegaId);

    // Nuevas operaciones basadas en InventoryService adaptado
    List<InventoryResponseDTO> getAllInventory();

    InventoryByBodegaDTO getInventoryByBodega(Long warehouseId);

    List<InventoryResponseDTO> getInventoryByProduct(Long productId);

    InventoryResponseDTO getSpecificInventory(Long warehouseId, Long productId);

    MovementResponseDTO registerMovement(Long warehouseId, MovementCreateDTO dto);

    List<MovementResponseDTO> registerBatchMovement(BatchMovementDTO dto);

    List<MovementResponseDTO> getWarehouseMovements(Long warehouseId);

    List<MovementResponseDTO> getProductMovements(Long productId);
}
