package com.example.backend_ci_soap.CONTROLLER;

import com.example.backend_ci_soap.DTO.ArticuloEntradaDTO;
import com.example.backend_ci_soap.DTO.ArticuloSalidaDTO;
import com.example.backend_ci_soap.REPOSITORY.ArticuloRepository;
import com.example.backend_ci_soap.SERVICE.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articulos")
public class ArticuloController {


    private ArticuloService articuloService;

    @Autowired
    public ArticuloController(ArticuloService articuloService) {
        this.articuloService = articuloService;
    }

    //Listar todos los articulos
    @GetMapping
    public List<ArticuloSalidaDTO> listarArticulos() {
        return articuloService.listarArticulos();
    }

    // Buscar un articulo por codigo
    @GetMapping("/{codigo}")
    public ResponseEntity<ArticuloSalidaDTO> buscarArticuloPorCodigo(@PathVariable String codigo) {
        return articuloService.buscarArticuloPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Guarda un nuevo articulo
    @PostMapping
    public ResponseEntity<ArticuloSalidaDTO> crearArticulo(@RequestBody ArticuloEntradaDTO articulo) {
        ArticuloSalidaDTO nuevoArticulo = articuloService.crearArticulo(articulo);
        return ResponseEntity.ok(nuevoArticulo);
    }

    //Actualiza un articulo existente
    @PutMapping("/{codigo}")
    public ResponseEntity<ArticuloSalidaDTO> actualizarArticulo(@PathVariable String codigo, @RequestBody ArticuloEntradaDTO articulo) {
        return articuloService.actualizarArticulo(codigo, articulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Elimina un articulo por codigo
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminarArticulo(@PathVariable String codigo) {
        boolean eliminado = articuloService.eliminarArticuloPorCodigo(codigo);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
