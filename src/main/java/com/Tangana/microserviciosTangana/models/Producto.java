package com.Tangana.microserviciosTangana.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data // Lombok genera automáticamente los Getters, Setters y Constructores
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // El ID se autogenera (1, 2, 3...)
    private Long id;

    private String name;       // Coincide con tu frontend
    private Integer price;     // Usamos Integer porque en tu JS eran enteros
    private String category;
    private Integer stock;

    // Para guardar la lista de URLs de imágenes (["img1.jpg", "img2.jpg"])
    // JPA creará una tabla secundaria llamada 'producto_images' para esto.
    @ElementCollection
    @CollectionTable(name = "producto_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;
}