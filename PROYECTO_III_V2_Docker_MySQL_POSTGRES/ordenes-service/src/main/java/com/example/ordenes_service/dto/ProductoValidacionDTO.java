package com.example.ordenes_service.dto;

public class ProductoValidacionDTO  {

    private Long id;
    private String sku;
    private String name;
    private String status; // espera "ACTIVE" | "INACTIVE" según producto-service

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método utilitario esperado por el cliente
    public boolean getActive() {
        return status != null && "ACTIVE".equalsIgnoreCase(status);
    }
}
