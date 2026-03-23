package com.smartcart.cart_checkoutservice.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveStockRequestDto {
    Long variantId;
    Integer quantity;
}
