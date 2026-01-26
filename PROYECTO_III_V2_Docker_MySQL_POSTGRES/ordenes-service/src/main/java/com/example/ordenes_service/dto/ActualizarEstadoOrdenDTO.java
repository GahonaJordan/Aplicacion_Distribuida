package com.example.ordenes_service.dto;

import jakarta.validation.constraints.*;

public class ActualizarEstadoOrdenDTO  {

    @NotNull(message = "Nuevo estado es obligatorio")
    private EstadoOrden nuevoEstado;

    @Size(max = 500, message = "Notas no pueden exceder 500 caracteres")
    private String notas;

    // Getters y Setters

    public EstadoOrden getNuevoEstado() {
        return nuevoEstado;
    }

    public void setNuevoEstado(EstadoOrden nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
