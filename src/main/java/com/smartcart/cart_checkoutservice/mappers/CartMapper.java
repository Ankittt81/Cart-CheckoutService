package com.smartcart.cart_checkoutservice.mappers;

import com.smartcart.cart_checkoutservice.client.ProductResponseDto;
import com.smartcart.cart_checkoutservice.client.VariantResponseDto;
import com.smartcart.cart_checkoutservice.dtos.CartItemResponseDto;
import com.smartcart.cart_checkoutservice.dtos.CartResponseDto;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class CartMapper {

    public CartItem toEntity(Integer Quantity, VariantResponseDto  variant, ProductResponseDto product) {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(UUID.randomUUID().toString());
        cartItem.setVariantId(variant.getVariantId());
        cartItem.setProductId(variant.getProductId());
        cartItem.setProductTitle(product.getTitle());
        cartItem.setProductImageUrl(product.getImageUrl());
        cartItem.setAttributes(variant.getAttributes());
        cartItem.setQuantity(Quantity);
        cartItem.setPriceAtAddition(BigDecimal.valueOf(variant.getPrice()));
        cartItem.setAddedAt(LocalDateTime.now());

        return cartItem;
    }
    public CartItemResponseDto toItemDto(CartItem cartItem) {
        CartItemResponseDto cartItemResponseDto = new CartItemResponseDto();
        cartItemResponseDto.setCartItemId(cartItem.getCartItemId());
        cartItemResponseDto.setVariantId(cartItem.getVariantId());
        cartItemResponseDto.setProductId(cartItem.getProductId());
        cartItemResponseDto.setProductTitle(cartItem.getProductTitle());
        cartItemResponseDto.setProductImageUrl(cartItem.getProductImageUrl());
        cartItemResponseDto.setAttributes(cartItem.getAttributes());
        cartItemResponseDto.setQuantity(cartItem.getQuantity());
        cartItemResponseDto.setPrice(cartItem.getPriceAtAddition());
        cartItemResponseDto.setAddedAt(cartItem.getAddedAt());
        cartItemResponseDto.setSubtotal(cartItem.getPriceAtAddition().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        return cartItemResponseDto;
    }

    public CartResponseDto toDto(Cart cart){
        CartResponseDto cartResponse = new CartResponseDto();
        cartResponse.setCartId(cart.getCartId());
        cartResponse.setUserId(cart.getUserId());
        cartResponse.setUserName(cart.getUserName());

        List<CartItemResponseDto> cartItemResponseDtos =cart.getCartItems()
                .stream().map(item->toItemDto(item)).toList();
        cartResponse.setCartItems(cartItemResponseDtos);

        cartResponse.setTotalAmount(cart.getTotalAmount());
        cartResponse.setCartStatus(cart.getCartStatus());
        cartResponse.setCreatedAt(cart.getCreatedAt());
        cartResponse.setLastModifiedAt(cart.getLastModifiedAt());
        return cartResponse;
    }
}
