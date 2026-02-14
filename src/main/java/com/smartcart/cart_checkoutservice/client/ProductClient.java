package com.smartcart.cart_checkoutservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {

    @GetMapping("variants/{variantId}")
    VariantResponseDto getVariantByVariantId(@PathVariable("variantId") Long variantId);
}
