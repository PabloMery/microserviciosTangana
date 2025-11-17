package com.Tangana.microserviciosTangana.Controller;

import com.Tangana.microserviciosTangana.DTO.AddItemRequest;
import com.Tangana.microserviciosTangana.Model.Carrito;
import com.Tangana.microserviciosTangana.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public Carrito getCart(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestHeader(value = "cart-token", required = false) String cartToken
    ) {
        return cartService.getCart(userId, cartToken);
    }

    @PostMapping("/add")
    public Carrito addItem(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestHeader("cart-token") String cartToken,
            @RequestBody AddItemRequest request
    ) {
        Carrito cart = cartService.getCart(userId, cartToken);
        return cartService.addItem(cart, request.getProductId(), request.getQuantity());
    }

    @DeleteMapping("/clear")
    public void clearCart(
            @RequestHeader(value = "userId", required = false) String userId,
            @RequestHeader("cart-token") String cartToken
    ) {
        Carrito cart = cartService.getCart(userId, cartToken);
        cartService.clearCart(cart);
    }
}
