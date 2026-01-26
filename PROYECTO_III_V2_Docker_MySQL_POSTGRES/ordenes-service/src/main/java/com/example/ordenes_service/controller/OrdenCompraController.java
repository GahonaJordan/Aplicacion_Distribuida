package com.example.ordenes_service.controller;

import com.example.ordenes_service.dto.ActualizarEstadoOrdenDTO;
import com.example.ordenes_service.dto.OrdenCompraCreateDTO;
import com.example.ordenes_service.dto.OrdenCompraResponseDTO;
import com.example.ordenes_service.dto.OrdenCompraResumenDTO;
import com.example.ordenes_service.entity.EstadoOrden;
import com.example.ordenes_service.service.OrdenesServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrdenCompraController {

    private final OrdenesServiceImpl ordenService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OrdenCompraResponseDTO> crearOrdenCompra(
            @Valid @RequestBody OrdenCompraCreateDTO dto) {

        OrdenCompraResponseDTO creada = ordenService.crearOrdenCompra(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<OrdenCompraResponseDTO>> obtenerTodasOrdenes() {
        List<OrdenCompraResponseDTO> ordenes = ordenService.obtenerTodasOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/resumen")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<OrdenCompraResumenDTO>> obtenerResumenOrdenes() {
        List<OrdenCompraResumenDTO> ordenes = ordenService.obtenerResumenOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerOrdenPorId(@PathVariable Long id) {
        OrdenCompraResponseDTO orden = ordenService.obtenerOrdenPorId(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/numero/{numeroOrden}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerOrdenPorNumero(@PathVariable String numeroOrden) {
        OrdenCompraResponseDTO orden = ordenService.obtenerOrdenPorNumero(numeroOrden);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<OrdenCompraResumenDTO>> obtenerOrdenesPorEstado(@PathVariable EstadoOrden estado) {
        List<OrdenCompraResumenDTO> ordenes = ordenService.obtenerOrdenesPorEstado(estado);
        return ResponseEntity.ok(ordenes);
    }

    @PatchMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponseDTO> aprobarOrden(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoOrdenDTO dto) {

        OrdenCompraResponseDTO aprobada = ordenService.aprobarOrden(id, dto);
        return ResponseEntity.ok(aprobada);
    }

    @PatchMapping("/{id}/recibir")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponseDTO> recibirOrden(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoOrdenDTO dto) {

        OrdenCompraResponseDTO recibida = ordenService.recibirOrden(id, dto);
        return ResponseEntity.ok(recibida);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponseDTO> cancelarOrden(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoOrdenDTO dto) {

        OrdenCompraResponseDTO cancelada = ordenService.cancelarOrden(id, dto);
        return ResponseEntity.ok(cancelada);
    }
}
