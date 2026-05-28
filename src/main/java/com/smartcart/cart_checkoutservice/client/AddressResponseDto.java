package com.smartcart.cart_checkoutservice.client;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponseDto {
    private Long id;
    private String fullName;
    private String mobile;
    private String alternateMobile;
    private String houseNo;
    private String area;
    private String landmark;
    private String city;
    private String state;
    private String pincode;
}
