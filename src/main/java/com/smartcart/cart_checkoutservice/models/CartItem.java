package com.smartcart.cart_checkoutservice.models;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem extends BaseModel {
    private Cart cart;
    private Long variantId;
    private Integer quantity;
    private Double priceSnapshot;

}

/*
     1                    1
   cartitem             cart =>M:1
      M                   1

 */
