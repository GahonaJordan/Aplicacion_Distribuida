package com.example.inventario_service.dto;

import java.util.List;

public class InventoryByBodegaDTO {

    private Long warehouseId;
    private String warehouseName;
    private List<ProductoStockDTO> products;

    // Getters and Setters

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public List<ProductoStockDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductoStockDTO> products) {
        this.products = products;
    }
}
