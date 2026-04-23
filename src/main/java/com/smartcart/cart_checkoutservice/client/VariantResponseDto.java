package com.smartcart.cart_checkoutservice.client;


import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class VariantResponseDto {
    private Long variantId;
    private Long productId;
    private String productTitle;
    private String sku;
    private Map<String,String> attributes;
    private Double price;
}
