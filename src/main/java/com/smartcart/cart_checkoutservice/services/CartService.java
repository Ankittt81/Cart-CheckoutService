package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.dtos.*;

public interface CartService {
     CartResponseDto addItem(Long userId, String userName, AddItemToCartRequest addItemToCartRequest);
     CartResponseDto getCart(Long userId);
     CartValidationResponse validateCart(Long userId);
    CartResponseDto updateItem(Long userId, Long variantId, Integer quantity);
    void removeItem(Long userId,Long variantId);
    void clearCart(Long userId);
    CartSummaryDto  getCartSummary(Long userId);
    CartValidationResult validateCartInternal(Long userId);

}
