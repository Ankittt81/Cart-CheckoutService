package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartItemResponse;

public interface CartService {
    public CartItemResponse addItem(Long userId,AddItemToCartRequest addItemToCartRequest);
}
