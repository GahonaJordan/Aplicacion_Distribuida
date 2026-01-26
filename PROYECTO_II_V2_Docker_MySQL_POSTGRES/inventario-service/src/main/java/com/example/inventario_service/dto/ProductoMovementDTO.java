package com.example.inventario_service.dto;

import jakarta.validation.constraints.*;

public class ProductoMovementDTO {

    @NotNull(message = "ID del producto es obligatorio")
    @Positive(message = "ID del producto debe ser positivo")
    private Long productId;

    @NotBlank(message = "SKU es obligatorio")
    private String sku;

    @NotNull(message = "Cantidad es obligatoria")
    @Positive(message = "Cantidad debe ser positiva")
    private Integer quantity;

    // Getters y setters

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
}
