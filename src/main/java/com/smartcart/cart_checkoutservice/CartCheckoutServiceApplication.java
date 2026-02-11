package com.smartcart.cart_checkoutservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
public class CartCheckoutServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartCheckoutServiceApplication.class, args);
    }

}
