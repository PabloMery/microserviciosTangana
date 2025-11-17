package com.Tangana.microserviciosTangana.service.impl;

import com.Tangana.microserviciosTangana.model.Producto;
import com.Tangana.microserviciosTangana.repository.ProductoRepository;
import com.Tangana.microserviciosTangana.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional // Gestiona las transacciones de la base de datos automáticamente
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;

    // --- Métodos de Lectura (Read) ---
    
    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByCategory(String categoria) {
        return repository.findByCategory(categoria);
    }

    // --- Métodos de Escritura (Create, Update, Delete) ---
    
    @Override
    public Producto create(Producto producto) {
        // Lógica de validación (¡mejora del 'demo'!)
        if (producto.getPrice() != null && producto.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        producto.setId(null); // Aseguramos que es una creación
        return repository.save(producto);
    }

    @Override
    public List<Producto> createBatch(List<Producto> productos) {
        // Validamos cada producto en el lote
        for (Producto p : productos) {
            if (p.getPrice() != null && p.getPrice() < 0) {
                throw new IllegalArgumentException("El producto '" + p.getName() + "' tiene un precio negativo.");
            }
            p.setId(null);
        }
        return repository.saveAll(productos);
    }

    @Override
    public Producto update(Long id, Producto productoDetalles) {
        // Lógica de actualización (¡mejora del 'demo'!)
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));

        if (productoDetalles.getPrice() != null && productoDetalles.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        
        // Mapeamos todos los campos de nuestra entidad
        existente.setName(productoDetalles.getName());
        existente.setPrice(productoDetalles.getPrice());
        existente.setCategory(productoDetalles.getCategory());
        existente.setStock(productoDetalles.getStock());
        existente.setImages(productoDetalles.getImages());

        return repository.save(existente);
    }

    @Override
    public void deleteById(Long id) {
        // Lógica de validación (¡mejora del 'demo'!)
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }
}