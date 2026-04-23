package com.smartcart.cart_checkoutservice.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal {
    private Long userId;
    private String username;

    public UserPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
