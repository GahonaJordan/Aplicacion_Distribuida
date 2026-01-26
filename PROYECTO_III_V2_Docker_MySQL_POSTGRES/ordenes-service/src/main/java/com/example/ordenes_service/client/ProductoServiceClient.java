package com.example.ordenes_service.client;

import com.example.ordenes_service.dto.ProductoValidacionDTO;
import com.example.ordenes_service.exception.ProductServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductoServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.product.url}")
    private String productServiceUrl; // p.e. http://localhost:9090

    private String baseUrl() {
        return productServiceUrl != null ? productServiceUrl : "";
    }

    public ProductoValidacionDTO getProduct(Long productoId) {
        String url = String.format("%s/api/productos/%d", baseUrl(), productoId);
        log.info("Consultando producto en: {}", url);
        try {
            ResponseEntity<ProductoValidacionDTO> resp = restTemplate.getForEntity(url, ProductoValidacionDTO.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                return resp.getBody();
            }
            Integer status = resp.getStatusCodeValue();
            log.error("Respuesta inesperada del Product Service: {} para producto {}", status, productoId);
            throw new ProductServiceException(status, "Respuesta inesperada del Product Service: " + resp.getStatusCode());
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Producto no encontrado: {}", productoId);
            throw new ProductServiceException(404, "Producto no encontrado con ID: " + productoId);
        } catch (HttpClientErrorException.Forbidden e) {
            log.warn("Acceso denegado al Product Service para producto {}: {}", productoId, e.getResponseBodyAsString());
            throw new ProductServiceException(403, "Acceso denegado al Product Service: " + e.getResponseBodyAsString(), e);
        } catch (HttpClientErrorException e) {
            Integer status = e.getStatusCode() != null ? e.getStatusCode().value() : null;
            log.error("Error HTTP al consultar producto: {} - {} - {}", productoId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ProductServiceException(status, "Error al consultar Product Service: " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            log.error("Error de conexión al consultar producto: {}", e.getMessage());
            throw new ProductServiceException(null, "Error al comunicarse con Product Service: " + e.getMessage(), e);
        }
    }

    public void validarProducto(Long productoId) {
        ProductoValidacionDTO producto = getProduct(productoId);
        if (!Boolean.TRUE.equals(producto.getActive())) {
            throw new ProductServiceException("El producto con ID " + productoId + " está inactivo");
        }
    }
}
