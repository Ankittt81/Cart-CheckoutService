package com.smartcart.cart_checkoutservice.models;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "carts")
public class Cart {
    @Id
    private Long userId;
    private String userName;
    private CartStatus cartStatus;
    private List<CartItem> cartItems=new ArrayList<>();
    private Double totalAmount=0.0;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date lastModifiedAt;
}


/*
     1                 M
    Cart             CartItem  =>1:M
     1                   1
 */