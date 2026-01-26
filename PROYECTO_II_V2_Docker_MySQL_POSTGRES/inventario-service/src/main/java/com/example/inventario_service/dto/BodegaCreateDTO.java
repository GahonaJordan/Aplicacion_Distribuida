package com.example.inventario_service.dto;

import com.example.inventario_service.entities.Bodega;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class BodegaCreateDTO {

    @NotBlank(message = "Código es obligatorio")
    @Size(max = 50, message = "Código no puede exceder 50 caracteres")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Código solo puede contener letras mayúsculas, números y guiones")
    private String code;

    @NotBlank(message = "Nombre es obligatorio")
    @Size(max = 200, message = "Nombre no puede exceder 200 caracteres")
    private String name;

    @Size(max = 500, message = "Dirección no puede exceder 500 caracteres")
    private String address;

    @Size(max = 50, message = "Ciudad no puede exceder 50 caracteres")
    private String city;

    @NotNull(message = "Estado activo es obligatorio")
    private Bodega.ProductStatus status;
    // Getters y Setters

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
}
