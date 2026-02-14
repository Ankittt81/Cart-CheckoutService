package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.client.ProductClient;
import com.smartcart.cart_checkoutservice.client.VariantResponseDto;
import com.smartcart.cart_checkoutservice.dtos.AddItemToCartRequest;
import com.smartcart.cart_checkoutservice.dtos.CartResponse;
import com.smartcart.cart_checkoutservice.mappers.CartMapper;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import com.smartcart.cart_checkoutservice.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private CartRepository cartRepository;
    private CartMapper  cartMapper;
    private final ProductClient  productClient;


    public CartServiceImpl(CartRepository cartRepository,CartMapper cartMapper,ProductClient productClient) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productClient = productClient;
    }

    @Override
    public CartResponse addItem(Long userId,String userName, AddItemToCartRequest addItemToCartRequest) {
        VariantResponseDto variant=productClient.getVariantByVariantId(addItemToCartRequest.getVariantId());
        CartItem newItem = cartMapper.toEntity(addItemToCartRequest.getQuantity(),variant);

        //1. first find cart of user if present else create new one
        Cart cart=null;
        Optional<Cart> cartOptional = cartRepository.findByUserId(userId);
        if(cartOptional.isEmpty()){
            cart=new Cart();
            cart.setUserId(userId);
            cart.setUserName(userName);
            cart.setCartStatus(CartStatus.ACTIVE);
        }else cart=cartOptional.get();

        //2. Fetching CartItems from Cart and Check the item ia adding is already exist or not
        Optional<CartItem> existingItem=cart.getCartItems()
                .stream()
                .filter(item ->item.getVariantId().equals(newItem.getVariantId()))
                .findFirst();

        //3. If exist then add quantity else add item to cart
        if(existingItem.isPresent()){
            CartItem cartItem=existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity()+newItem.getQuantity());
        }else cart.getCartItems().add(newItem);

        double total=cart.getCartItems().stream()
                .mapToDouble(i ->i.getPriceSnapshot()*i.getQuantity())
                .sum();
        cart.setTotalAmount(total);


        return cartMapper.toDto(cartRepository.save(cart));
    }
}
