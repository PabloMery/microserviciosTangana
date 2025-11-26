package com.Tangana.microserviciosTangana.DTO;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}
