package com.Tangana.microserviciosTangana.DTO;

import lombok.Data;

@Data
public class AddItemRequest {
    private String productId;
    private int quantity;
}