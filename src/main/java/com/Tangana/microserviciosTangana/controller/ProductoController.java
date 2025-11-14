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
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorCategoria(
            @RequestParam(name = "categoria") String categoryName) {
        
        // Llamamos al nuevo método que creamos en el repositorio
        List<Producto> productos = productoRepository.findByCategory(categoryName);
        
        return ResponseEntity.ok(productos);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    productoRepository.delete(producto);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // 3. Crear un producto nuevo (útil para poblar la base de datos desde Postman)
    // POST http://localhost:8080/api/productos
    @PostMapping
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoRepository.save(producto);
    }
    @PostMapping("/batch") // Lo ponemos en una ruta diferente: /api/productos/batch
    public List<Producto> crearMultiplesProductos(@RequestBody List<Producto> productos) {
        // JpaRepository tiene un método saveAll() perfecto para esto
        return productoRepository.saveAll(productos); 
    }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id, 
            @RequestBody Producto productoDetalles) {

        // 1. Buscamos el producto en la BD por su ID
        return productoRepository.findById(id)
            .map(productoExistente -> {
                
                // 2. Si existe, actualizamos todos sus campos
                //    con los datos que vienen en el "body" (productoDetalles)
                productoExistente.setName(productoDetalles.getName());
                productoExistente.setPrice(productoDetalles.getPrice());
                productoExistente.setCategory(productoDetalles.getCategory());
                productoExistente.setStock(productoDetalles.getStock());
                productoExistente.setImages(productoDetalles.getImages());

                // 3. Guardamos los cambios en la BD.
                //    Como 'productoExistente' ya tiene un ID, JPA sabe
                //    que debe hacer un UPDATE y no un INSERT.
                Producto productoActualizado = productoRepository.save(productoExistente);
                
                // 4. Devolvemos el producto actualizado
                return ResponseEntity.ok(productoActualizado);

            })
            // 5. Si findById no encuentra nada, devolvemos un 404 Not Found
            .orElse(ResponseEntity.notFound().build());
    }
}