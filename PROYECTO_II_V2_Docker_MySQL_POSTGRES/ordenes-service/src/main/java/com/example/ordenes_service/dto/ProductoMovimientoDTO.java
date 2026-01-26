package com.example.ordenes_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductoMovimientoDTO {

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("quantity")
    private Integer quantity;

    public ProductoMovimientoDTO() {
    }

    public ProductoMovimientoDTO(Long productId, String sku, Integer quantity) {
        this.productId = productId;
        this.sku = sku;
        this.quantity = quantity;
    }

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
