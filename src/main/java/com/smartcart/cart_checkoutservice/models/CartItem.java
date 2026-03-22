package com.smartcart.cart_checkoutservice.models;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CartItem {
    private Long variantId;
    private Integer quantity;
    private BigDecimal priceSnapshot;
    private String productName;
    private Date addedAt;

}

/*
     1                    1
   cartitem             cart =>M:1
      M                   1

 */
