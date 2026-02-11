package com.smartcart.cart_checkoutservice.models;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "carts")
public class Cart extends BaseModel {
    private Long userId;
    private CartStatus cartStatus;
    private List<CartItem> cartItems=new ArrayList<>();
    private Double totalAmount=0.0;
}


/*
     1                 M
    Cart             CartItem  =>1:M
     1                   1
 */