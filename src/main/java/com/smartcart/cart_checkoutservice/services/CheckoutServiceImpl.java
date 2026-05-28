package com.smartcart.cart_checkoutservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcart.cart_checkoutservice.client.AddressClient;
import com.smartcart.cart_checkoutservice.client.AddressResponseDto;
import com.smartcart.cart_checkoutservice.client.InventoryClient;
import com.smartcart.cart_checkoutservice.client.ReserveStockRequestDto;
import com.smartcart.cart_checkoutservice.dtos.ApiResponse;
import com.smartcart.cart_checkoutservice.dtos.CartValidationResult;
import com.smartcart.cart_checkoutservice.events.CheckoutEvent;
import com.smartcart.cart_checkoutservice.events.ShippingAddress;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import com.smartcart.cart_checkoutservice.repositories.CartRepository;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private CartService cartService;
    private InventoryClient  inventoryClient;
    private KafkaTemplate<String,String> kafkaTemplate;
    private CartRepository  cartRepository;
    private ObjectMapper objectMapper;
    private AddressClient addressClient;

    public CheckoutServiceImpl(CartService cartService, InventoryClient inventoryClient,KafkaTemplate<String, String> kafkaTemplate, CartRepository cartRepository, ObjectMapper objectMapper,AddressClient addressClient) {
        this.cartService = cartService;
        this.inventoryClient = inventoryClient;
        this.kafkaTemplate = kafkaTemplate;
        this.cartRepository = cartRepository;
        this.objectMapper = objectMapper;
        this.addressClient = addressClient;
    }

    @Override
    @Transactional
    public String checkout(Long userId,Long addressId) {
        ApiResponse<AddressResponseDto> addressResponse=addressClient.getAddressById(addressId);
        if(addressResponse == null || addressResponse.getData() == null){
            throw new RuntimeException("Invalid address");
        }
        AddressResponseDto address = addressResponse.getData();

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setId(address.getId());
        shippingAddress.setFullName(address.getFullName());
        shippingAddress.setMobile(address.getMobile());
        shippingAddress.setAlternateMobile(address.getAlternateMobile());
        shippingAddress.setHouseNo(address.getHouseNo());
        shippingAddress.setArea(address.getArea());
        shippingAddress.setLandmark(address.getLandmark());
        shippingAddress.setCity(address.getCity());
        shippingAddress.setState(address.getState());
        shippingAddress.setPincode(address.getPincode());

        //Validate Cart
        CartValidationResult result= cartService.validateCartInternal(userId);

        if (result.getUpdatedcart().getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty after validation");
        }
        Cart cart= result.getUpdatedcart();

        // reserve stock
        for(CartItem item : cart.getCartItems()) {
            ReserveStockRequestDto dto = new ReserveStockRequestDto();
            dto.setQuantity(item.getQuantity());
            dto.setVariantId(item.getVariantId());
            boolean reserved = inventoryClient.reserveStock(dto);

            if (!reserved) {
                throw new RuntimeException("Stock reservation failed");
            }
        }
        String checkoutId= UUID.randomUUID().toString();

        CheckoutEvent checkoutEvent = new CheckoutEvent();
        checkoutEvent.setUserId(userId);
        checkoutEvent.setCheckoutId(checkoutId);
        checkoutEvent.setShippingAddress(shippingAddress);
        checkoutEvent.setCartItems(cart.getCartItems());
        checkoutEvent.setTotalAmount(cart.getTotalAmount());

        // 5. Send Kafka event
        try {
            String event = objectMapper.writeValueAsString(checkoutEvent);
            kafkaTemplate.send("checkout-topic", event);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }

        // 6. Update cart status
        cart.setCartStatus(CartStatus.CHECKOUT);
        cartRepository.save(cart);

        return checkoutId;
    }
}
