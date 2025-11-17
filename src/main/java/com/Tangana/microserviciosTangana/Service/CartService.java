package com.Tangana.microserviciosTangana.Service;

import com.Tangana.microserviciosTangana.Model.Carrito;
import com.Tangana.microserviciosTangana.Model.CartItem;
import com.Tangana.microserviciosTangana.Repository.CartRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Carrito getCart(String userId, String cartToken) {

        if (userId != null && !userId.isEmpty()) {
            return cartRepository.findByUserId(userId)
                    .orElseGet(() -> cartRepository.save(
                            new Carrito(null, userId, null, new java.util.ArrayList<>())
                    ));
        }

        if (cartToken != null && !cartToken.isEmpty()) {
            return cartRepository.findByCartToken(cartToken)
                    .orElseGet(() -> cartRepository.save(
                            new Carrito(null, null, cartToken, new java.util.ArrayList<>())
                    ));
        }

        // caso raro
        String generated = UUID.randomUUID().toString();
        return cartRepository.save(
                new Carrito(null, null, generated, new java.util.ArrayList<>())
        );
    }

    public Carrito addItem(Carrito cart, String productId, int quantity) {

        boolean exists = false;

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(item.getQuantity() + quantity);
                exists = true;
                break;
            }
        }

        if (!exists) {
            cart.getItems().add(new CartItem(null, productId, quantity));
        }

        return cartRepository.save(cart);
    }

    public void clearCart(Carrito cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
