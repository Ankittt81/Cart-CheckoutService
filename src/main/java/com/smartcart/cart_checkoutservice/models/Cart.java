package com.smartcart.cart_checkoutservice.models;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "carts")
public class Cart {
    @Id
    private String cartId;
    private Long userId;
    private String userName;
    private CartStatus cartStatus;
    private List<CartItem> cartItems=new ArrayList<>();
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
}


/*
     1                 M
    Cart             CartItem  =>1:M
     1                   1
 */