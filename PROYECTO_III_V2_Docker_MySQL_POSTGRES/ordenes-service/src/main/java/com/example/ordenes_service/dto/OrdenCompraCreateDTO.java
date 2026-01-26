package com.example.ordenes_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class OrdenCompraCreateDTO  {

    @NotNull(message = "ID del proveedor es obligatorio")
    @Positive(message = "ID del proveedor debe ser positivo")
    private Long proveedorId;

    @NotNull(message = "ID de la bodega es obligatorio")
    @Positive(message = "ID de la bodega debe ser positivo")
    private Long bodegaId;

    private LocalDate fechaEsperada;

    @Size(max = 1000, message = "Notas no pueden exceder 1000 caracteres")
    private String notas;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid
    private List<DetalleOrdenCompraDTO> detalles;

    // Getters y Setters

    public Long getProveedorId() {
        return proveedorId;
    }

    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long bodegaId) {
        this.bodegaId = bodegaId;
    }

    public LocalDate getFechaEsperada() {
        return fechaEsperada;
    }

    public void setFechaEsperada(LocalDate fechaEsperada) {
        this.fechaEsperada = fechaEsperada;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public List<DetalleOrdenCompraDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleOrdenCompraDTO> detalles) {
        this.detalles = detalles;
    }
}
