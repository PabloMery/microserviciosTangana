package com.Tangana.microserviciosTangana.DTO;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long productId;
    private Integer quantity;
}