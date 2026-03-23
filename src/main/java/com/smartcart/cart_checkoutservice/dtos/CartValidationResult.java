package com.smartcart.cart_checkoutservice.dtos;

import com.smartcart.cart_checkoutservice.models.Cart;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartValidationResult {
    private Cart  updatedcart;
    private List<String> issues;
}
