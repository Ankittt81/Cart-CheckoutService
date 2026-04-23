package com.smartcart.cart_checkoutservice.repositories;

import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart,Long> {

    Optional<Cart> findByUserIdAndCartStatus(Long userId, CartStatus cartStatus);
    List<Cart> findByUserId(Long userId);

    Cart save(Cart cart);
    Cart deleteByUserId(Long userId);
}
