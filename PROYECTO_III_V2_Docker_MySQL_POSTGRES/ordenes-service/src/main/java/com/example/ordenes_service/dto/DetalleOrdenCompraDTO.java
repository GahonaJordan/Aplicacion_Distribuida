package com.example.ordenes_service.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class DetalleOrdenCompraDTO  {

    @NotNull(message = "ID del producto es obligatorio")
    @Positive(message = "ID del producto debe ser positivo")
    private Long productoId;

    @NotBlank(message = "SKU es obligatorio")
    @Size(max = 50, message = "SKU no puede exceder 50 caracteres")
    private String sku;

    @NotNull(message = "Cantidad es obligatoria")
    @Positive(message = "Cantidad debe ser positiva")
    private Integer cantidad;

    @NotNull(message = "Precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "Precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "Precio debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal precioUnitario;

    // Getters y Setters

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}
