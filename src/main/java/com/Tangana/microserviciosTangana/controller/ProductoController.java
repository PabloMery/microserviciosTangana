package com.Tangana.microserviciosTangana.controller;

import com.Tangana.microserviciosTangana.model.Producto;
import com.Tangana.microserviciosTangana.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
// Esto permite que tu React (en el puerto 5173) consuma esta API sin bloqueos
@CrossOrigin(origins = "http://localhost:5173") 
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Obtener todos los productos
    // GET http://localhost:8080/api/productos
    @GetMapping
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // 2. Obtener un producto por ID (para la vista de detalle)
    // GET http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> ResponseEntity.ok(producto))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear un producto nuevo (útil para poblar la base de datos desde Postman)
    // POST http://localhost:8080/api/productos
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }
}