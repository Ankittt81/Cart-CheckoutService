package com.smartcart.cart_checkoutservice.mappers;

import com.smartcart.cart_checkoutservice.client.VariantResponseDto;
import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class CartMapper {

    public CartItem toEntity(Integer Quantity, VariantResponseDto  variant) {
        CartItem cartItem = new CartItem();
       cartItem.setVariantId(variant.getVariantId());
       cartItem.setProductId(variant.getProductId());
       cartItem.setQuantity(Quantity);
       cartItem.setPriceSnapshot(BigDecimal.valueOf(variant.getPrice()));
       cartItem.setAttributes(variant.getAttributes());
       cartItem.setProductName(variant.getProductTitle());
       cartItem.setAddedAt(LocalDateTime.now());
        return cartItem;
    }

    public CartResponse toDto(Cart cart){
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItems(cart.getCartItems());
        cartResponse.setTotalAmount(cart.getTotalAmount());
        cartResponse.setCartId(cart.getCartId());
        cartResponse.setUserId(cart.getUserId());
        cartResponse.setUser_name(cart.getUserName());
        cartResponse.setStatus(cart.getCartStatus());
        return cartResponse;
    }
}
