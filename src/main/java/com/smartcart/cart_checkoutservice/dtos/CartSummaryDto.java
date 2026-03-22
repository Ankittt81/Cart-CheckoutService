package com.smartcart.cart_checkoutservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class CartSummaryDto {
    private Long _id;
    private Integer totalItems;
    private Integer totalQuantity;
    private BigDecimal totalAmount=BigDecimal.ZERO;

}
