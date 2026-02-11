package com.smartcart.cart_checkoutservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemToCartRequest {
    private Long variantId;
    private Double priceSnapshot;
    private Integer quantity;
}
