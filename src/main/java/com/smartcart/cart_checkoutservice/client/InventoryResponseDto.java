package com.smartcart.cart_checkoutservice.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryResponseDto {
    private Long inventoryId;
    private Long variantId;
    private String sku;
    private Integer availableStock;
    private Integer reservedStock;
    private boolean active;
}
