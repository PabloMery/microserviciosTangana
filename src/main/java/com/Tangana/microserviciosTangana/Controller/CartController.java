package com.Tangana.microserviciosTangana.Controller;

import com.Tangana.microserviciosTangana.DTO.CarritoDTO;
import com.Tangana.microserviciosTangana.DTO.CartItemRequest;
import com.Tangana.microserviciosTangana.DTO.UpdateItemRequest;
import com.Tangana.microserviciosTangana.Service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(@RequestHeader("X-User-Id") Long userId) {
        if (userId == null) return ResponseEntity.badRequest().body("Falta X-User-Id");
        return ResponseEntity.ok(cartService.getOrCreateOpenCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest req
    ) {
        return ResponseEntity.ok(cartService.addItem(userId, req.getProductId(), req.getQuantity()));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<?> updateItem(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId,
            @RequestBody UpdateItemRequest req
    ) {
        return ResponseEntity.ok(cartService.updateItem(userId, productId, req.getQuantity()));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<?> removeItem(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId
    ) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @PostMapping("/merge")
    public ResponseEntity<?> mergeCart(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody List<CartItemRequest> items
    ) {
        return ResponseEntity.ok(cartService.mergeCart(userId, items));
    }
}