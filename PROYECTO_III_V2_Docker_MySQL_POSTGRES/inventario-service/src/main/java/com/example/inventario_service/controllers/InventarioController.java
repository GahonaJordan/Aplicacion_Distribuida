// language: java
package com.example.inventario_service.controllers;

import com.example.inventario_service.dto.*;
import com.example.inventario_service.services.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        List<InventoryResponseDTO> inventory = inventarioService.getAllInventory();
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/bodega/{bodegaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<InventoryByBodegaDTO> getInventoryByBodega(@PathVariable Long bodegaId) {
        InventoryByBodegaDTO inventory = inventarioService.getInventoryByBodega(bodegaId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/producto/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<InventoryResponseDTO>> getInventoryByProduct(@PathVariable Long productId) {
        List<InventoryResponseDTO> inventory = inventarioService.getInventoryByProduct(productId);
        return ResponseEntity.ok(inventory);
    }

    @GetMapping("/bodega/{bodegaId}/producto/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<InventoryResponseDTO> getSpecificInventory(
            @PathVariable Long bodegaId,
            @PathVariable Long productId) {

        InventoryResponseDTO inventory = inventarioService.getSpecificInventory(bodegaId, productId);
        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/bodega/{bodegaId}/movimientos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<MovementResponseDTO> registerMovement(
            @PathVariable Long bodegaId,
            @Valid @RequestBody MovementCreateDTO dto) {

        MovementResponseDTO movement = inventarioService.registerMovement(bodegaId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movement);
    }

    @PostMapping("/movimientos/batch")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<MovementResponseDTO>> registerBatchMovement(
            @Valid @RequestBody BatchMovementDTO dto) {

        List<MovementResponseDTO> movements = inventarioService.registerBatchMovement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movements);
    }

    @GetMapping("/bodega/{bodegaId}/movimientos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<MovementResponseDTO>> getBodegaMovements(@PathVariable Long bodegaId) {
        List<MovementResponseDTO> movements = inventarioService.getWarehouseMovements(bodegaId);
        return ResponseEntity.ok(movements);
    }

    @GetMapping("/producto/{productId}/movimientos")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<MovementResponseDTO>> getProductMovements(@PathVariable Long productId) {
        List<MovementResponseDTO> movements = inventarioService.getProductMovements(productId);
        return ResponseEntity.ok(movements);
    }
}
