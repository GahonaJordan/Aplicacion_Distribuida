package com.example.ordenes_service.dto;

import java.util.List;

public class MovimientoInventarioRequestDTO {

    private Long warehouseId;
    private TipoMovimiento type;
    private String referenceType;
    private Long referenceId;
    private String notes;
    private List<ProductoMovimientoDTO> products;

    // Getters and Setters

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public TipoMovimiento getType() {
        return type;
    }

    public void setType(TipoMovimiento type) {
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

    public List<ProductoMovimientoDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductoMovimientoDTO> products) {
        this.products = products;
    }
}
