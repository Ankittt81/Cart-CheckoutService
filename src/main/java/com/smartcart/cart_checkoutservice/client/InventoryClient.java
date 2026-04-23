package com.smartcart.cart_checkoutservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",url = "${inventory.service.url}")
public interface InventoryClient {
    @GetMapping("/inventory/{variantId}")
    InventoryResponseDto checkStock(@PathVariable("variantId") Long variantId);

    @PostMapping("/inventory/reserve")
    boolean reserveStock(@RequestBody ReserveStockRequestDto reserveStockRequestDto);
}
