package com.Tangana.microserviciosTangana.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Tangana.microserviciosTangana.Model.CartItem;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

