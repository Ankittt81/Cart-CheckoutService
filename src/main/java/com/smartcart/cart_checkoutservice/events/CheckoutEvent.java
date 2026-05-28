package com.smartcart.cart_checkoutservice.events;

import com.smartcart.cart_checkoutservice.models.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CheckoutEvent {
    private Long userId;
    private String checkoutId;
    private ShippingAddress shippingAddress;
    private List<CartItem> cartItems;
    private BigDecimal totalAmount;
}
