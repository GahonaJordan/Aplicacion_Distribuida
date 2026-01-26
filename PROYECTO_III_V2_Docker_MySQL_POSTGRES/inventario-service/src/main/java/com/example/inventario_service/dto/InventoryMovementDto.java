package com.example.inventario_service.dto;

import jakarta.validation.constraints.*;

public class InventoryMovementDto {

    @NotNull(message = "productId es obligatorio")
    private Long productId;

    @NotBlank(message = "sku es obligatorio")
    private String sku;

    @NotNull(message = "quantity es obligatorio")
    @Min(value = -1000000, message = "quantity inválido") // permitido negativo para salidas
    @Max(value = 1000000, message = "quantity inválido")
    private Integer quantity; // positivo para ingreso, negativo para salida

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
