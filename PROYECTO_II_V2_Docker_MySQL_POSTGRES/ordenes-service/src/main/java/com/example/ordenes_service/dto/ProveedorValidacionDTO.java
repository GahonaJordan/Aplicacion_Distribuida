package com.example.ordenes_service.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ProveedorValidacionDTO  {

    private Long id;

    @JsonAlias({"businessName", "name"})
    private String businessName;

    private String status;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método utilitario esperado por el cliente
    public boolean getActive() {
        return status != null && "ACTIVE".equalsIgnoreCase(status);
    }
}
