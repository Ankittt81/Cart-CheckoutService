package com.smartcart.cart_checkoutservice.dtos;

import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private String cartId;
    private Long userId;
    private String user_name;
    private List<CartItem> cartItems=new ArrayList<>();
    private BigDecimal totalAmount=BigDecimal.ZERO;
    private CartStatus status;
}
