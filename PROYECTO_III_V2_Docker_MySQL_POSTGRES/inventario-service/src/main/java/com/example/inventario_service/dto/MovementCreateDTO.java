package com.example.inventario_service.dto;

import jakarta.validation.constraints.*;

public class MovementCreateDTO {

    @NotNull(message = "ID del producto es obligatorio")
    @Positive(message = "ID del producto debe ser positivo")
    private Long productId;

    @NotBlank(message = "SKU es obligatorio")
    @Size(max = 50, message = "SKU no puede exceder 50 caracteres")
    private String sku;

    @NotNull(message = "Cantidad es obligatoria")
    @Positive(message = "Cantidad debe ser positiva")
    private Integer quantity;

    @NotNull(message = "Tipo de movimiento es obligatorio")
    private MovementType type;

    @Size(max = 50, message = "Tipo de referencia no puede exceder 50 caracteres")
    private String referenceType;

    private Long referenceId;

    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notes;

    // Getters y Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
}
