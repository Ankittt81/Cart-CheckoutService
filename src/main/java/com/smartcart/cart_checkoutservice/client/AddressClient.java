package com.smartcart.cart_checkoutservice.client;

import com.smartcart.cart_checkoutservice.dtos.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service",url = "${user.service.url}")
public interface AddressClient {
    @GetMapping("/addresses/{id}")
    ApiResponse<AddressResponseDto> getAddressById(@PathVariable Long id);
}
