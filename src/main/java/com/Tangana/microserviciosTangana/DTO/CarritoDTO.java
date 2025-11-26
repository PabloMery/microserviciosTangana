package com.Tangana.microserviciosTangana.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

import com.Tangana.microserviciosTangana.Model.CarritoStats;

@Data
public class CarritoDTO {

    private Long id;
    private Long userId;
    private CarritoStats status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemDTO> items;
}