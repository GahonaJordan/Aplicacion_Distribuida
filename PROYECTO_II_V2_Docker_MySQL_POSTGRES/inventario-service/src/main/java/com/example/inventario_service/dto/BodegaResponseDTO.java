package com.example.inventario_service.dto;

import com.example.inventario_service.entities.Bodega;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class BodegaResponseDTO {

    private Long id;
    private String code;
    private String name;
    private String address;
    private String city;
    private Bodega.ProductStatus status;
    private LocalDateTime createdAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Bodega.ProductStatus getStatus() {
        return status;
    }

    public void setStatus(Bodega.ProductStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
