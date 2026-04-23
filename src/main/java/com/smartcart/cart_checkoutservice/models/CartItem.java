package com.smartcart.cart_checkoutservice.models;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class CartItem {
    private Long variantId;
    private Long productId;
    private Integer quantity;
    private BigDecimal priceSnapshot;
    private String productName;
    private Map<String,String> attributes;
    private LocalDateTime addedAt;

}

/*
     1                    1
   cartitem             cart =>M:1
      M                   1

 */
