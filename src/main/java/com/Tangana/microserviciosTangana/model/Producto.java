package com.Tangana.microserviciosTangana.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;



@Entity
@Data 
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío") 
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres") 
    @Column(nullable = false, length = 150) 
    private String name;

    @NotNull(message = "El precio es obligatorio") 
    @PositiveOrZero(message = "El precio no puede ser negativo")
    @Column(nullable = false)
    private Integer price;

    @NotBlank(message = "La categoría no puede estar vacía")
    @Size(max = 50)
    @Column(nullable = false, length = 50)     
    private String category;
    
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;


    @ElementCollection
    @CollectionTable(name = "producto_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;
}