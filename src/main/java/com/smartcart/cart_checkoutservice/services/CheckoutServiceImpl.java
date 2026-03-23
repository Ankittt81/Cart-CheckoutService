package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.client.InventoryClient;
import com.smartcart.cart_checkoutservice.client.ReserveStockRequestDto;
import com.smartcart.cart_checkoutservice.dtos.CartValidationResponse;
import com.smartcart.cart_checkoutservice.dtos.CartValidationResult;
import com.smartcart.cart_checkoutservice.events.CheckoutEvent;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import com.smartcart.cart_checkoutservice.repositories.CartRepository;
import org.springframework.kafka.core.KafkaTemplate;

public class CheckoutServiceImpl implements CheckoutService {
    private CartService cartService;
    private InventoryClient  inventoryClient;
    private KafkaTemplate<String,CheckoutEvent> kafkaTemplate;
    private CartRepository  cartRepository;

    public CheckoutServiceImpl(CartService cartService, InventoryClient inventoryClient,KafkaTemplate<String, CheckoutEvent> kafkaTemplate, CartRepository cartRepository) {
        this.cartService = cartService;
        this.inventoryClient = inventoryClient;
        this.kafkaTemplate = kafkaTemplate;
        this.cartRepository = cartRepository;
    }

    @Override
    public String checkout(Long userId) {
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
            boolean reserved=inventoryClient.reserveStock(dto);

            if(!reserved) {
                throw new RuntimeException("Stock reservation failed");
            }

            CheckoutEvent checkoutEvent = new CheckoutEvent();
            checkoutEvent.setUserId(userId);
            checkoutEvent.setCartItems(cart.getCartItems());
            checkoutEvent.setTotalAmount(cart.getTotalAmount());

            // 5. Send Kafka event
            kafkaTemplate.send("checkout-topic", checkoutEvent);

            // 6. Update cart status
            cart.setCartStatus(CartStatus.CHECKOUT);
            cartRepository.save(cart);
        }
        return "Checkout successful";
    }
}
