package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;

public interface CartService {
    public CartResponse addItem(Long userId,String userName, AddItemToCartRequest addItemToCartRequest);
}
