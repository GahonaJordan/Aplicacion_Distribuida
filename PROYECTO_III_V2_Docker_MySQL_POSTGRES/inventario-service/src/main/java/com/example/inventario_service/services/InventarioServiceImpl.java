// language: java
package com.example.inventario_service.services;

import com.example.inventario_service.dto.*;
import com.example.inventario_service.entities.Bodega;
import com.example.inventario_service.entities.Inventario;
import com.example.inventario_service.entities.InventoryMovement;
import com.example.inventario_service.exceptions.ResourceNotFoundException;
import com.example.inventario_service.exceptions.InvalidOperationException;
import com.example.inventario_service.repositories.BodegaRepository;
import com.example.inventario_service.repositories.InventarioRepository;
import com.example.inventario_service.repositories.InventoryMovementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioServiceImpl implements InventarioService {

    private static final Logger log = LoggerFactory.getLogger(InventarioServiceImpl.class);

    private final InventarioRepository repo;
    private final BodegaRepository bodegaRepository;
    private final InventoryMovementRepository movementRepository;


    public InventarioServiceImpl(InventarioRepository repo,
                                 BodegaRepository bodegaRepository,
                                 InventoryMovementRepository movementRepository) {
        this.repo = repo;
        this.bodegaRepository = bodegaRepository;
        this.movementRepository = movementRepository;
    }

    // --- legacy methods kept ---
    @Override
    public void addMovements(Long bodegaId, List<InventoryMovementDto> movements) {
        if (movements == null || movements.isEmpty()) return;

        // validar existencia de bodega
        Bodega bodega = bodegaRepository.findById(bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + bodegaId));

        for (InventoryMovementDto m : movements) {
            if (m.getProductId() == null || m.getQuantity() == null) {
                throw new InvalidOperationException("Movimiento inválido");
            }
            if (m.getQuantity() == 0) continue;

            Inventario inv = repo.findByProductIdAndBodegaId_Id(m.getProductId(), bodegaId)
                    .orElseGet(() -> {
                        Inventario n = new Inventario();
                        n.setProductId(m.getProductId());
                        n.setSku(m.getSku());
                        n.setBodegaId(bodega); // pasar entidad Bodega
                        n.setQuantity(0);
                        n.setUpdatedAt(LocalDateTime.now());
                        return n;
                    });

            long newStock = (long) inv.getQuantity() + m.getQuantity();
            if (newStock < 0) {
                throw new InvalidOperationException("Stock insuficiente para producto " + m.getProductId() + " en bodega " + bodegaId);
            }
            inv.setQuantity((int) newStock);
            inv.setUpdatedAt(LocalDateTime.now());
            repo.save(inv);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inventario> findByBodegaId(Long bodegaId) {
        return repo.findByBodegaId_Id(bodegaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Inventario findByProductAndBodega(Long productId, Long bodegaId) {
        return repo.findByProductIdAndBodegaId_Id(productId, bodegaId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado para producto " + productId + " en bodega " + bodegaId));
    }

    // --- nuevas operaciones adaptadas ---
    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getAllInventory() {
        List<Inventario> all = new ArrayList<>();
        repo.findAll().forEach(all::add);
        return all.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryByBodegaDTO getInventoryByBodega(Long warehouseId) {
        Bodega warehouse = bodegaRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + warehouseId));

        List<Inventario> inventories = repo.findByBodegaId_Id(warehouseId);

        List<ProductoStockDTO> products = inventories.stream()
                .map(inv -> {
                    ProductoStockDTO p = new ProductoStockDTO();
                    p.setProductId(inv.getProductId());
                    p.setSku(inv.getSku());
                    p.setQuantity(inv.getQuantity());
                    return p;
                }).collect(Collectors.toList());

        InventoryByBodegaDTO res = new InventoryByBodegaDTO();
        res.setWarehouseId(warehouse.getId());
        res.setWarehouseName(warehouse.getName());
        res.setProducts(products);
        return res;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponseDTO> getInventoryByProduct(Long productId) {
        return repo.findByProductId(productId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDTO getSpecificInventory(Long warehouseId, Long productId) {
        Inventario inventory = repo.findByProductIdAndBodegaId_Id(productId, warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe inventario para producto " + productId + " en bodega " + warehouseId));
        return mapToResponseDTO(inventory);
    }

    @Override
    public MovementResponseDTO registerMovement(Long warehouseId, MovementCreateDTO dto) {
        log.info("Registrando movimiento {} para producto {} en bodega {}", dto.getType(), dto.getProductId(), warehouseId);

        Bodega warehouse = bodegaRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + warehouseId));

        Inventario inventory = repo.findByProductIdAndBodegaId_Id(dto.getProductId(), warehouseId)
                .orElseGet(() -> {
                    Inventario newInv = new Inventario();
                    newInv.setBodegaId(warehouse);
                    newInv.setProductId(dto.getProductId());
                    newInv.setSku(dto.getSku());
                    newInv.setQuantity(0);
                    newInv.setUpdatedAt(LocalDateTime.now());
                    return newInv;
                });

        if (dto.getType() == com.example.inventario_service.dto.MovementType.ENTRADA) {
            inventory.setQuantity(inventory.getQuantity() + dto.getQuantity());
        } else if (dto.getType() == com.example.inventario_service.dto.MovementType.SALIDA) {
            if (inventory.getQuantity() < dto.getQuantity()) {
                throw new InvalidOperationException("Stock insuficiente. Disponible: " + inventory.getQuantity() + ", Solicitado: " + dto.getQuantity());
            }
            inventory.setQuantity(inventory.getQuantity() - dto.getQuantity());
        }

        inventory.setUpdatedAt(LocalDateTime.now());
        repo.save(inventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setBodegaId(warehouse);
        movement.setProductId(dto.getProductId());
        movement.setSku(dto.getSku());
        // convertir enum DTO -> entidad
        movement.setType(com.example.inventario_service.entities.MovementType.valueOf(dto.getType().name()));
        movement.setQuantity(dto.getQuantity());
        movement.setReferenceType(dto.getReferenceType());
        movement.setReferenceId(dto.getReferenceId());
        movement.setNotes(dto.getNotes());

        InventoryMovement savedMovement = movementRepository.save(movement);

        log.info("Movimiento registrado. Stock actual: {}", inventory.getQuantity());

        return mapMovementToResponseDTO(savedMovement);
    }

    @Override
    public List<MovementResponseDTO> registerBatchMovement(BatchMovementDTO dto) {
        log.info("Registrando movimiento batch de {} productos en bodega {}", dto.getProducts().size(), dto.getBodegaId());

        Bodega warehouse = bodegaRepository.findById(dto.getBodegaId())
                .orElseThrow(() -> new ResourceNotFoundException("Bodega no encontrada con ID: " + dto.getBodegaId()));

        List<MovementResponseDTO> responses = new ArrayList<>();

        for (ProductoMovementDTO product : dto.getProducts()) {
            Inventario inventory = repo.findByProductIdAndBodegaId_Id(product.getProductId(), dto.getBodegaId())
                    .orElseGet(() -> {
                        Inventario newInv = new Inventario();
                        newInv.setBodegaId(warehouse);
                        newInv.setProductId(product.getProductId());
                        newInv.setSku(product.getSku());
                        newInv.setQuantity(0);
                        newInv.setUpdatedAt(LocalDateTime.now());
                        return newInv;
                    });

            if (dto.getType() == MovementType.ENTRADA) {
                inventory.setQuantity(inventory.getQuantity() + product.getQuantity());
            } else if (dto.getType() == MovementType.SALIDA) {
                if (inventory.getQuantity() < product.getQuantity()) {
                    throw new InvalidOperationException("Stock insuficiente para producto " + product.getProductId() +
                            ". Disponible: " + inventory.getQuantity() + ", Solicitado: " + product.getQuantity());
                }
                inventory.setQuantity(inventory.getQuantity() - product.getQuantity());
            }

            inventory.setUpdatedAt(LocalDateTime.now());
            repo.save(inventory);

            InventoryMovement movement = new InventoryMovement();
            movement.setBodegaId(warehouse);
            movement.setProductId(product.getProductId());
            movement.setSku(product.getSku());
            movement.setType(com.example.inventario_service.entities.MovementType.valueOf(dto.getType().name()));
            movement.setQuantity(product.getQuantity());
            movement.setReferenceType(dto.getReferenceType());
            movement.setReferenceId(dto.getReferenceId());
            movement.setNotes(dto.getNotes());

            InventoryMovement savedMovement = movementRepository.save(movement);
            responses.add(mapMovementToResponseDTO(savedMovement));
        }

        log.info("Movimiento batch completado: {} productos procesados", responses.size());

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDTO> getWarehouseMovements(Long warehouseId) {
        return movementRepository.findByBodegaId_Id(warehouseId)
                .stream()
                .map(this::mapMovementToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDTO> getProductMovements(Long productId) {
        return movementRepository.findByProductId(productId)
                .stream()
                .map(this::mapMovementToResponseDTO)
                .collect(Collectors.toList());
    }

    // --- mapeos ---
    private InventoryResponseDTO mapToResponseDTO(Inventario inventory) {
        InventoryResponseDTO r = new InventoryResponseDTO();
        r.setId(inventory.getId());
        r.setWarehouseId(inventory.getBodegaId() != null ? inventory.getBodegaId().getId() : null);
        r.setWarehouseName(inventory.getBodegaId() != null ? inventory.getBodegaId().getName() : null);
        r.setProductId(inventory.getProductId());
        r.setSku(inventory.getSku());
        r.setQuantity(inventory.getQuantity());
        r.setUpdatedAt(inventory.getUpdatedAt());
        return r;
    }

    private MovementResponseDTO mapMovementToResponseDTO(InventoryMovement movement) {
        MovementResponseDTO r = new MovementResponseDTO();
        r.setId(movement.getId());
        r.setWarehouseId(movement.getBodegaId() != null ? movement.getBodegaId().getId() : null);
        r.setWarehouseName(movement.getBodegaId() != null ? movement.getBodegaId().getName() : null);
        r.setProductId(movement.getProductId());
        r.setSku(movement.getSku());
        r.setType(movement.getType() != null ? com.example.inventario_service.dto.MovementType.valueOf(movement.getType().name()) : null);
        r.setQuantity(movement.getQuantity());
        r.setReferenceType(movement.getReferenceType());
        r.setReferenceId(movement.getReferenceId());
        r.setNotes(movement.getNotes());
        r.setCreatedAt(movement.getCreatedAt());
        return r;
    }

}
