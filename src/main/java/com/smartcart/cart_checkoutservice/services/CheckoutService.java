package com.smartcart.cart_checkoutservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CheckoutService {
    String checkout(Long userId);
}
