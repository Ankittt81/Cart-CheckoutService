package com.smartcart.cart_checkoutservice.dtos;

import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponseDto {
    private String cartId;
    private Long userId;
    private String userName;
    private List<CartItemResponseDto> cartItems;
    private BigDecimal totalAmount;
    private CartStatus cartStatus;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
