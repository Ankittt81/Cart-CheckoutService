package com.smartcart.cart_checkoutservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;
import com.smartcart.cart_checkoutservice.security.UserPrincipal;
import com.smartcart.cart_checkoutservice.services.CartService;
import com.smartcart.cart_checkoutservice.services.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/cart")
public class CartController {
    private CartService cartService;
    private CheckoutService checkoutService;

    public CartController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    @PostMapping("/items")
    public CartResponse addItemToCart(@AuthenticationPrincipal UserPrincipal user,@Valid @RequestBody AddItemToCartRequest addItemToCartRequest) {
        return cartService.addItem(user.getUserId(), user.getUsername(), addItemToCartRequest);
    }

    @GetMapping("/checkout")
    public ResponseEntity<String> checkout(@AuthenticationPrincipal UserPrincipal user){
       String response= checkoutService.checkout(user.getUserId());
        return ResponseEntity.ok().body(response);
    }
}
