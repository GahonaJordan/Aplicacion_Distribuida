package com.example.proveedor_service.dto;

import com.example.proveedor_service.entities.Proveedor;
import jakarta.validation.constraints.*;

public class ProveedorDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El NIT/RUT/CUIT es obligatorio")
    private String taxId;

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Size(max = 50)
    private String phone;

    @Size(max = 500)
    private String address;

    @NotNull
    public enum ProductStatus { ACTIVE, INACTIVE }
    private Proveedor.ProductStatus status;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Proveedor.ProductStatus getStatus() { return status; }
    public void setStatus(Proveedor.ProductStatus status) { this.status = status; }
}
