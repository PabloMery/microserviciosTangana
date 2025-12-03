package com.Tangana.microserviciosTangana.controller;

import com.Tangana.microserviciosTangana.model.Producto;
import com.Tangana.microserviciosTangana.service.ProductoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; 

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoService service;

    
    @GetMapping
    public List<Producto> obtenerTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<Producto> opt = service.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(opt.get());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @RequestParam(name = "categoria") String categoryName) {
        return ResponseEntity.ok(service.findByCategory(categoryName));
    }


    @PostMapping
    public ResponseEntity<?> crearProducto(@Valid @RequestBody Producto producto) { 

        try {
            Producto nuevo = service.create(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    @PostMapping("/batch")
    public ResponseEntity<?> crearMultiplesProductos(@RequestBody List<Producto> productos) {
        try {
            List<Producto> nuevos = service.createBatch(productos);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevos);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id, 
            @Valid @RequestBody Producto productoDetalles) { 
        
        
        try {
            Producto actualizado = service.update(id, productoDetalles);
            return ResponseEntity.ok(actualizado);
        } catch (IllegalArgumentException ex) {
            if (ex.getMessage().contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}