package com.Tangana.microserviciosTangana.service;

import com.Tangana.microserviciosTangana.model.Producto;
import com.Tangana.microserviciosTangana.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    @Autowired
    private FileService fileService; 


    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Producto> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Producto> findByCategory(String category) {
        return repository.findByCategory(category);
    }


    public Producto create(Producto producto) {
        if (producto.getPrice() != null && producto.getPrice() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        producto.setId(null);
        return repository.save(producto);
    }

    public List<Producto> createBatch(List<Producto> productos) {
        return repository.saveAll(productos);
    }

    public Producto update(Long id, Producto productoDetalles) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));


        borrarFotosHuérfanas(existente.getImages(), productoDetalles.getImages());

        existente.setName(productoDetalles.getName());
        existente.setPrice(productoDetalles.getPrice());
        existente.setCategory(productoDetalles.getCategory());
        existente.setStock(productoDetalles.getStock());
        existente.setImages(productoDetalles.getImages());

        return repository.save(existente);
    }

    public void deleteById(Long id) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        if (existente.getImages() != null) {
            for (String url : existente.getImages()) {
                borrarImagenDelDisco(url); 
            }
        }

        repository.deleteById(id);
    }



    private void borrarFotosHuérfanas(List<String> viejas, List<String> nuevas) {
        if (viejas == null) return;
        
        for (String fotoVieja : viejas) {
            if (nuevas == null || !nuevas.contains(fotoVieja)) {
                borrarImagenDelDisco(fotoVieja);
            }
        }
    }

    private void borrarImagenDelDisco(String url) {

        if (url != null && url.startsWith("/api/files/")) {

            String filename = url.substring("/api/files/".length());
            
            fileService.delete(filename);
        }
    }
}