package com.example.inventario_service.dto;

import jakarta.validation.constraints.*;

import java.util.List;


public class BatchMovementDTO {

    @NotNull(message = "ID de bodega es obligatorio")
    @Positive(message = "ID de bodega debe ser positivo")
    private Long bodegaId;

    @NotNull(message = "Tipo de movimiento es obligatorio")
    private MovementType type;

    @Size(max = 50, message = "Tipo de referencia no puede exceder 50 caracteres")
    private String referenceType;

    private Long referenceId;

    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notes;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<ProductoMovementDTO> products;

    // Getters y Setters

    public Long getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Long warehouseId) {
        this.bodegaId = warehouseId;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<ProductoMovementDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductoMovementDTO> products) {
        this.products = products;
    }
}
