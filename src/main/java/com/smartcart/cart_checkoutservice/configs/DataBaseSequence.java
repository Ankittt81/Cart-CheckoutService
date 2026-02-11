package com.smartcart.cart_checkoutservice.configs;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Data
@Getter
@Setter
@Document(collection = "database_sequences")
public class DataBaseSequence {
    @Id
    private String id;
    private Long seq;
}
