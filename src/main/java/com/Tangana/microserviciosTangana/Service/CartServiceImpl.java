package com.Tangana.microserviciosTangana.Service;

import com.Tangana.microserviciosTangana.DTO.CarritoDTO;
import com.Tangana.microserviciosTangana.DTO.CartItemDTO;
import com.Tangana.microserviciosTangana.DTO.CartItemRequest;
import com.Tangana.microserviciosTangana.Model.Carrito;
import com.Tangana.microserviciosTangana.Model.CartItem;
import com.Tangana.microserviciosTangana.Model.CarritoStats;
import com.Tangana.microserviciosTangana.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    // ==========================
    //   MÉTODOS PÚBLICOS
    // ==========================

    @Override
    @Transactional
    public CarritoDTO getOrCreateOpenCart(Long userId) {
        Carrito carrito = findOrCreateOpenCart(userId);
        return toDto(carrito);
    }

    @Override
    public CarritoDTO addItem(Long userId, Long productId, int quantity) {
        if (quantity == 0) {
            // No hacemos nada si la cantidad a agregar es 0
            return getOrCreateOpenCart(userId);
        }

        Carrito carrito = findOrCreateOpenCart(userId);

        CartItem item = carrito.getItems().stream()
                .filter(ci -> ci.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            if (quantity > 0) {
                CartItem newItem = new CartItem();
                newItem.setCarrito(carrito);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                carrito.getItems().add(newItem);
            }
        } else {
            int newQty = item.getQuantity() + quantity;
            if (newQty <= 0) {
                // Si al sumar queda 0 o menos, se elimina del carrito
                carrito.getItems().remove(item);
            } else {
                item.setQuantity(newQty);
            }
        }

        Carrito saved = cartRepository.save(carrito);
        return toDto(saved);
    }

    @Override
    public CarritoDTO updateItem(Long userId, Long productId, int quantity) {
        Carrito carrito = findOrCreateOpenCart(userId);

        CartItem item = carrito.getItems().stream()
                .filter(ci -> ci.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            if (quantity <= 0) {
                // Nada que hacer si no existía y la cantidad es 0 o negativa
                return toDto(carrito);
            }
            // Crear nuevo item con la cantidad exacta
            CartItem newItem = new CartItem();
            newItem.setCarrito(carrito);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            carrito.getItems().add(newItem);
        } else {
            if (quantity <= 0) {
                // Eliminamos el item del carrito si la nueva cantidad es 0 o menos
                carrito.getItems().remove(item);
            } else {
                item.setQuantity(quantity);
            }
        }

        Carrito saved = cartRepository.save(carrito);
        return toDto(saved);
    }

    @Override
    public CarritoDTO removeItem(Long userId, Long productId) {
        Carrito carrito = findOrCreateOpenCart(userId);

        carrito.getItems().removeIf(ci -> ci.getProductId().equals(productId));

        Carrito saved = cartRepository.save(carrito);
        return toDto(saved);
    }

    @Override
    public CarritoDTO clearCart(Long userId) {
        Carrito carrito = findOrCreateOpenCart(userId);
        carrito.getItems().clear();
        Carrito saved = cartRepository.save(carrito);
        return toDto(saved);
    }

    @Override
    public CarritoDTO mergeCart(Long userId, List<CartItemRequest> itemsFromClient) {
        Carrito carrito = findOrCreateOpenCart(userId);

        if (itemsFromClient == null || itemsFromClient.isEmpty()) {
            return toDto(carrito);
        }

        for (CartItemRequest req : itemsFromClient) {
            if (req == null || req.getProductId() == null || req.getQuantity() == null) {
                continue;
            }

            Long productId = req.getProductId();
            int quantity = req.getQuantity();

            if (quantity <= 0) {
                continue;
            }

            // Buscamos si ya existe ese producto en el carrito
            CartItem item = carrito.getItems().stream()
                    .filter(ci -> ci.getProductId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (item == null) {
                // Si no existe, lo agregamos con la cantidad que viene del cliente
                CartItem newItem = new CartItem();
                newItem.setCarrito(carrito);
                newItem.setProductId(productId);
                newItem.setQuantity(quantity);
                carrito.getItems().add(newItem);
            } else {
                // Si existe, MERGE: sumamos cantidades
                int newQty = item.getQuantity() + quantity;
                item.setQuantity(newQty);
            }
        }

        Carrito saved = cartRepository.save(carrito);
        return toDto(saved);
    }

    // ==========================
    //   MÉTODOS PRIVADOS
    // ==========================

    /**
     * Busca el carrito OPEN del usuario. Si no existe, lo crea y lo guarda.
     */
    private Carrito findOrCreateOpenCart(Long userId) {
        Optional<Carrito> opt = cartRepository.findByUserIdAndStatus(userId, CarritoStats.OPEN);
        if (opt.isPresent()) {
            return opt.get();
        }

        Carrito carrito = new Carrito();
        carrito.setUserId(userId);
        carrito.setStatus(CarritoStats.OPEN);
        carrito.setItems(new ArrayList<>());

        return cartRepository.save(carrito);
    }

    /**
     * Convierte la entidad Carrito en un CarritoDTO para el frontend.
     */
    private CarritoDTO toDto(Carrito carrito) {
        CarritoDTO dto = new CarritoDTO();
        dto.setId(carrito.getId());
        dto.setUserId(carrito.getUserId());
        dto.setStatus(carrito.getStatus());
        dto.setCreatedAt(carrito.getCreatedAt());
        dto.setUpdatedAt(carrito.getUpdatedAt());

        List<CartItemDTO> itemsDto = new ArrayList<>();
        if (carrito.getItems() != null) {
            for (CartItem item : carrito.getItems()) {
                CartItemDTO ci = new CartItemDTO();
                ci.setProductId(item.getProductId());
                ci.setQuantity(item.getQuantity());
                itemsDto.add(ci);
            }
        }
        dto.setItems(itemsDto);

        return dto;
    }
}
