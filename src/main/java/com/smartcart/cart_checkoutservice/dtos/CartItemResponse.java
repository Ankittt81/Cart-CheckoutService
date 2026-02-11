package com.smartcart.cart_checkoutservice.dtos;

import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartItemResponse {
    private Long userId;
    private Long cartId;
    private List<CartItem> cartItems=new ArrayList<>();
    private Double totalAmount=0.0;
    private CartStatus status;
}
