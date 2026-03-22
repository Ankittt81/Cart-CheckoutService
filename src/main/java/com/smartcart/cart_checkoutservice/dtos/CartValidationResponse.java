package com.smartcart.cart_checkoutservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartValidationResponse {
    private boolean valid;
    private List<String> issues;
    private CartResponse updatedCart;
}
