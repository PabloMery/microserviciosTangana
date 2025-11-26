package com.Tangana.microserviciosTangana.Service;

import com.Tangana.microserviciosTangana.DTO.CarritoDTO;
import com.Tangana.microserviciosTangana.DTO.CartItemRequest;

import java.util.List;

public interface CartService {

    CarritoDTO getOrCreateOpenCart(Long userId);

    CarritoDTO addItem(Long userId, Long productId, int quantity);

    CarritoDTO updateItem(Long userId, Long productId, int quantity);

    CarritoDTO removeItem(Long userId, Long productId);

    CarritoDTO clearCart(Long userId);

    CarritoDTO mergeCart(Long userId, List<CartItemRequest> itemsFromClient);
}
