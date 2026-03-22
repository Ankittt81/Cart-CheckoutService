package com.smartcart.cart_checkoutservice.repositories;

import com.smartcart.cart_checkoutservice.models.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepository extends MongoRepository<Cart,Long> {

    Optional<Cart> findByUserId(Long userId);

    Cart save(Cart cart);
    Cart deleteByUserId(Long userId);
}
