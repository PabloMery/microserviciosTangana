package com.Tangana.microserviciosTangana.repository;

import com.Tangana.microserviciosTangana.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Aquí puedes agregar métodos personalizados si los necesitas, por ejemplo:
    List<Producto> findByCategory(String category);
}