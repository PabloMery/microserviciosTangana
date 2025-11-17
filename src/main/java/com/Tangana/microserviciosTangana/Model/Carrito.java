package com.Tangana.microserviciosTangana.Model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name="cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String userId;

    @Column(nullable = true, unique = true)
    private String cartToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) 
    @JoinColumn(name = "cart_id")
    private List<CartItem> items = new ArrayList<>();

}
