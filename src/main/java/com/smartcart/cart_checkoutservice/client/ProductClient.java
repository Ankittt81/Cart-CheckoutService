package com.smartcart.cart_checkoutservice.client;

import com.smartcart.cart_checkoutservice.dtos.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${product.service.url}")
public interface ProductClient {

    @GetMapping("variants/{variantId}")
    ApiResponse<VariantResponseDto> getVariantByVariantId(@PathVariable("variantId") Long variantId);
    @GetMapping("products/{productId}")
    ApiResponse<ProductResponseDto> getSingleProduct(@PathVariable("productId") Long productId);
}
