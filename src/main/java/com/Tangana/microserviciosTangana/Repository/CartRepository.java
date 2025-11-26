package com.Tangana.microserviciosTangana.Repository;

import com.Tangana.microserviciosTangana.Model.Carrito;
import com.Tangana.microserviciosTangana.Model.CarritoStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUserIdAndStatus(Long userId, CarritoStats status);
}