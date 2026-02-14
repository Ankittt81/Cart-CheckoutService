package com.smartcart.cart_checkoutservice;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;


@SpringBootApplication
@EnableMongoAuditing
@EnableFeignClients
public class CartCheckoutServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CartCheckoutServiceApplication.class, args);
    }
    @PostConstruct
    public void test() {
        System.out.println("Mongo URI Loaded");
    }
}
