package com.Tangana.microserviciosTangana.controller;

import com.Tangana.microserviciosTangana.model.Producto;
// 1. Importamos el SERVICIO en lugar del Repositorio
import com.Tangana.microserviciosTangana.service.ProductoService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- Importamos HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional; // <-- Importamos Optional

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {

    // 2. Inyectamos el SERVICIO
    @Autowired
    private ProductoService service;

    // --- ENDPOINTS DE LECTURA ---
    
    @GetMapping
    public List<Producto> obtenerTodos() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        // La lógica de "Optional" se queda aquí, como en el 'demo'
        Optional<Producto> opt = service.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado con ID: " + id);
        }
        return ResponseEntity.ok(opt.get());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @RequestParam(name = "categoria") String categoryName) {
        // Este endpoint sigue siendo simple
        return ResponseEntity.ok(service.findByCategory(categoryName));
    }

    // --- ENDPOINTS DE ESCRITURA (con manejo de errores) ---

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        // 3. Usamos el patrón try...catch del 'demo'
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
            @RequestBody Producto productoDetalles) {
        // 4. Manejo de errores más específico para PUT
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