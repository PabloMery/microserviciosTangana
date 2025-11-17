package com.Tangana.microserviciosTangana.Repository;

import com.Tangana.microserviciosTangana.Model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUserId(String userId);

    Optional<Carrito> findByCartToken(String cartToken);
}