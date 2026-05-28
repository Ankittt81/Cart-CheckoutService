package com.smartcart.cart_checkoutservice.controllers;

import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.ApiResponse;
import com.smartcart.cart_checkoutservice.dtos.CartResponseDto;
import com.smartcart.cart_checkoutservice.dtos.CheckoutRequestDto;
import com.smartcart.cart_checkoutservice.models.Cart;
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
    public ResponseEntity<ApiResponse> addItemToCart(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody AddItemToCartRequest addItemToCartRequest) {
        CartResponseDto response= cartService.addItem(user.getUserId(), user.getUsername(), addItemToCartRequest);
        return ResponseEntity.ok(new ApiResponse("success", response));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getCart(@AuthenticationPrincipal UserPrincipal user) {
        CartResponseDto response= cartService.getCart(user.getUserId());
        return ResponseEntity.ok(new ApiResponse("success", response));
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse> checkout(@AuthenticationPrincipal UserPrincipal user,@RequestBody CheckoutRequestDto request) {
       String response= checkoutService.checkout(user.getUserId(), request.getAddressId());
        return ResponseEntity.ok(new ApiResponse<>("Checkout Successfull", response));
    }
}
