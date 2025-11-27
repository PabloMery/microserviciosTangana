package com.Tangana.microserviciosTangana.Controller;

import com.Tangana.microserviciosTangana.DTO.CarritoDTO;
import com.Tangana.microserviciosTangana.DTO.CartItemRequest;
import com.Tangana.microserviciosTangana.Service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@CrossOrigin(
    origins = "http://localhost:5173",      // tu frontend Vite
    allowedHeaders = "*",                   // incluye X-User-Id
    methods = { RequestMethod.GET, RequestMethod.POST,
                RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
@RestController
@RequestMapping("/api/cart")   // 👈 ESTE es el path base
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // GET /api/cart
    @GetMapping
    public ResponseEntity<CarritoDTO> getCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        CarritoDTO dto = cartService.getOrCreateOpenCart(userId);
        return ResponseEntity.ok(dto);
    }

    // POST /api/cart/items
    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> addItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest request
    ) {
        CarritoDTO dto = cartService.addItem(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(dto);
    }

    // PUT /api/cart/items
    @PutMapping("/items")
    public ResponseEntity<CarritoDTO> updateItem(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody CartItemRequest request
    ) {
        CarritoDTO dto = cartService.updateItem(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(dto);
    }

    // DELETE /api/cart/items/{productId}
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CarritoDTO> removeItem(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long productId
    ) {
        CarritoDTO dto = cartService.removeItem(userId, productId);
        return ResponseEntity.ok(dto);
    }

    // DELETE /api/cart
    @DeleteMapping
    public ResponseEntity<CarritoDTO> clearCart(
            @RequestHeader("X-User-Id") Long userId
    ) {
        CarritoDTO dto = cartService.clearCart(userId);
        return ResponseEntity.ok(dto);
    }

    // POST /api/cart/merge
    @PostMapping("/merge")
    public ResponseEntity<CarritoDTO> mergeCart(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody List<CartItemRequest> items
    ) {
        CarritoDTO dto = cartService.mergeCart(userId, items);
        return ResponseEntity.ok(dto);
    }
}
