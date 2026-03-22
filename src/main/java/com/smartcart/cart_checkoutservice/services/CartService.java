package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;
import com.smartcart.cart_checkoutservice.dtos.CartSummaryDto;
import com.smartcart.cart_checkoutservice.dtos.CartValidationResponse;

public interface CartService {
     CartResponse addItem(Long userId,String userName, AddItemToCartRequest addItemToCartRequest);
     CartResponse getCart(Long userId);
     CartValidationResponse validateCart(Long userId);
    CartResponse updateItem(Long userId,Long variantId,Integer quantity);
    void removeItem(Long userId,Long variantId);
    void clearCart(Long userId);
    CartSummaryDto  getCartSummary(Long userId);

}
