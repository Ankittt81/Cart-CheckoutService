package com.smartcart.cart_checkoutservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class CartItemResponseDto {
    private String cartItemId;
    private Long variantId;
    private Long productId;
    private String productTitle;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private Map<String,String> attributes;
    private LocalDateTime addedAt;
}
