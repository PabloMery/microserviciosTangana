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
    private FileService fileService; // Necesitamos esto para borrar archivos físicos

    // --- LECTURA ---

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

    // --- ESCRITURA ---

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

    // --- AQUÍ ESTÁ LA LÓGICA DE BORRADO AL ACTUALIZAR ---
    public Producto update(Long id, Producto productoDetalles) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        // 1. Detectar qué fotos se quitaron
        // Comparamos la lista vieja (existente) con la nueva (detalles)
        borrarFotosHuérfanas(existente.getImages(), productoDetalles.getImages()); // <--- ¡CLAVE!

        // 2. Actualizar datos
        existente.setName(productoDetalles.getName());
        existente.setPrice(productoDetalles.getPrice());
        existente.setCategory(productoDetalles.getCategory());
        existente.setStock(productoDetalles.getStock());
        existente.setImages(productoDetalles.getImages());

        return repository.save(existente);
    }

    // --- AQUÍ ESTÁ LA LÓGICA DE BORRADO AL ELIMINAR PRODUCTO ---
    public void deleteById(Long id) {
        Producto existente = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        // 1. Borrar TODAS las fotos del disco antes de borrar el producto
        if (existente.getImages() != null) {
            for (String url : existente.getImages()) {
                borrarImagenDelDisco(url); // <--- ¡CLAVE!
            }
        }

        // 2. Borrar de la base de datos
        repository.deleteById(id);
    }

    // --- FUNCIONES AUXILIARES ---

    /**
     * Compara la lista de fotos viejas con las nuevas.
     * Si una foto estaba antes pero ya no está, se borra del disco.
     */
    private void borrarFotosHuérfanas(List<String> viejas, List<String> nuevas) {
        if (viejas == null) return;
        
        for (String fotoVieja : viejas) {
            // Si la lista nueva es nula o NO contiene la foto vieja...
            if (nuevas == null || !nuevas.contains(fotoVieja)) {
                // ...significa que el usuario la borró. ¡A la basura!
                borrarImagenDelDisco(fotoVieja);
            }
        }
    }

    /**
     * Llama al FileService para borrar el archivo físico.
     */
    private void borrarImagenDelDisco(String url) {
        // Solo intentamos borrar si es una imagen subida por nosotros (del backend)
        // Las imágenes estáticas del frontend (/IMG/...) no se tocan.
        if (url != null && url.startsWith("/api/files/")) {
            // Quitamos el prefijo para tener solo el nombre de archivo
            // Ejemplo: "/api/files/12345_foto.jpg" -> "12345_foto.jpg"
            String filename = url.substring("/api/files/".length());
            
            // Borramos físicamente
            fileService.delete(filename);
        }
    }
}