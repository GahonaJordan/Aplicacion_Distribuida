package com.example.backend_ci_soap.Pruebas_Unitarias;

import com.example.backend_ci_soap.DTO.ArticuloEntradaDTO;
import com.example.backend_ci_soap.DTO.ArticuloSalidaDTO;
import com.example.backend_ci_soap.EXCEPTION.ArticuloException;
import com.example.backend_ci_soap.MODEL.Articulo;
import com.example.backend_ci_soap.REPOSITORY.ArticuloRepository;
import com.example.backend_ci_soap.SERVICE.ArticuloService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class Test_Articulo_Unitarias {

    private ArticuloRepository repo;
    private ArticuloService service;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(ArticuloRepository.class);
        service = new ArticuloService(repo);
    }

    @Test
    void crearArticulo_success() {
        ArticuloEntradaDTO dto = new ArticuloEntradaDTO(
                "C001",
                "Nombre",
                "Cat",
                new BigDecimal("10.00"),
                new BigDecimal("20.00"),
                5,
                1,
                "Prov"
        );

        when(repo.existsByCodigo("C001")).thenReturn(false);
        when(repo.save(any(Articulo.class))).thenAnswer(inv -> {
            Articulo a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        ArticuloSalidaDTO salida = service.crearArticulo(dto);

        assertThat(salida).isNotNull();
        assertThat(salida.getId()).isEqualTo(1L);
        assertThat(salida.getCodigo()).isEqualTo("C001");
        verify(repo, times(1)).save(any(Articulo.class));
    }

    @Test
    void crearArticulo_duplicateCode_throwsArticuloException() {
        ArticuloEntradaDTO dto = new ArticuloEntradaDTO(
                "C002",
                "Nombre",
                "Cat",
                new BigDecimal("10.00"),
                new BigDecimal("20.00"),
                5,
                1,
                "Prov"
        );

        when(repo.existsByCodigo("C002")).thenReturn(true);

        assertThrows(ArticuloException.class, () -> service.crearArticulo(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void crearArticulo_priceInvalid_throwsArticuloException() {
        ArticuloEntradaDTO dto = new ArticuloEntradaDTO(
                "C003",
                "Nombre",
                "Cat",
                new BigDecimal("10.00"),
                new BigDecimal("5.00"), // precioVenta <= precioCompra
                5,
                1,
                "Prov"
        );

        when(repo.existsByCodigo("C003")).thenReturn(false);

        assertThrows(ArticuloException.class, () -> service.crearArticulo(dto));
        verify(repo, never()).save(any());
    }

    @Test
    void crearArticulo_dataIntegrityViolation_wrappedAsIllegalState() {
        ArticuloEntradaDTO dto = new ArticuloEntradaDTO(
                "C004",
                "Nombre",
                "Cat",
                new BigDecimal("10.00"),
                new BigDecimal("20.00"),
                5,
                1,
                "Prov"
        );

        when(repo.existsByCodigo("C004")).thenReturn(false);
        when(repo.save(any())).thenThrow(new DataIntegrityViolationException("dup"));

        assertThrows(IllegalStateException.class, () -> service.crearArticulo(dto));
    }
}
