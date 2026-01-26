package com.example.ordenes_service.entity;

public enum EstadoOrden {
    PENDIENTE,   // Creada, esperando aprobación
    APROBADA,    // Aprobada, esperando recepción
    RECIBIDA,    // Recibida, inventario actualizado
    CANCELADA    // Cancelada
}