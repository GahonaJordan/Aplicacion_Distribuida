package com.example.inventario_service.controllers;

import com.example.inventario_service.dto.BodegaCreateDTO;
import com.example.inventario_service.dto.BodegaResponseDTO;
import com.example.inventario_service.dto.BodegaUpdateDTO;
import com.example.inventario_service.services.BodegaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponseDTO> createBodega(@Valid @RequestBody BodegaCreateDTO dto) {
        BodegaResponseDTO created = bodegaService.createBodega(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<BodegaResponseDTO>> getActiveBodegas() {
        return ResponseEntity.ok(bodegaService.getActiveBodegas());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<BodegaResponseDTO>> getAllBodegas() {
        return ResponseEntity.ok(bodegaService.getAllBodegas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BodegaResponseDTO> getBodegaById(@PathVariable Long id) {
        return ResponseEntity.ok(bodegaService.getBodegaById(id));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<BodegaResponseDTO> getBodegaByCode(@PathVariable String code) {
        return ResponseEntity.ok(bodegaService.getBodegaByCode(code));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BodegaResponseDTO> updateBodega(@PathVariable Long id,
                                                          @Valid @RequestBody BodegaUpdateDTO dto) {
        return ResponseEntity.ok(bodegaService.updateBodega(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBodega(@PathVariable Long id) {
        bodegaService.deleteBodega(id);
        return ResponseEntity.noContent().build();
    }
}
