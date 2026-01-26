package com.example.ordenes_service.dto;

public class BodegaValidacionDTO  {

    private Long id;
    private String name;
    private String status;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método utilitario esperado por el cliente
    public boolean getActive() {
        return status != null && "ACTIVE".equalsIgnoreCase(status);
    }
}
