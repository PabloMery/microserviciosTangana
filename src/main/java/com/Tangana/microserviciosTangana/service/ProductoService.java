package com.Tangana.microserviciosTangana.service;

import com.Tangana.microserviciosTangana.model.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz para la capa de servicio de Producto.
 * Abstrae la lógica de negocio del controlador.
 */
public interface ProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    List<Producto> findByCategory(String categoria);
    Producto create(Producto producto);
    List<Producto> createBatch(List<Producto> productos);
    Producto update(Long id, Producto producto);
    void deleteById(Long id);
}