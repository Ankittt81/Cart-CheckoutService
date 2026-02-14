package com.smartcart.cart_checkoutservice.controllers;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;
import com.smartcart.cart_checkoutservice.services.CartService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public CartResponse addItemToCart(@AuthenticationPrincipal Long userId,@AuthenticationPrincipal String userName, @Valid @RequestBody AddItemToCartRequest addItemToCartRequest) {
        return cartService.addItem(userId,userName, addItemToCartRequest);
    }
}
