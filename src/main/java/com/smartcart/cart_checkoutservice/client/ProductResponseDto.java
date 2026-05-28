package com.smartcart.cart_checkoutservice.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long productId;
    private String title;
    private String description;
    private Double basePrice;
    private String categoryTitle;
    private String imageUrl;
    private Long sellerId;
}