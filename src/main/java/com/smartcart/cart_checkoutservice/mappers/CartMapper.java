package com.smartcart.cart_checkoutservice.mappers;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartItemResponse;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartItem toEntity(AddItemToCartRequest  addItemToCartRequest) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(addItemToCartRequest.getQuantity());
        cartItem.setVariantId(addItemToCartRequest.getVariantId());
        cartItem.setPriceSnapshot(addItemToCartRequest.getPriceSnapshot());
        return cartItem;
    }

    public CartItemResponse toDto(Cart cart){
        CartItemResponse cartItemResponse = new CartItemResponse();
        cartItemResponse.setCartId(cart.getId());
        cartItemResponse.setCartItems(cart.getCartItems());
        cartItemResponse.setTotalAmount(cart.getTotalAmount());
        cartItemResponse.setUserId(cart.getUserId());
        cartItemResponse.setStatus(cart.getCartStatus());
        return cartItemResponse;
    }
}
