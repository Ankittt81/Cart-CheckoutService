package com.smartcart.cart_checkoutservice.services;

import com.smartcart.cart_checkoutservice.client.*;
import com.smartcart.cart_checkoutservice.dtos.*;
import com.smartcart.cart_checkoutservice.mappers.CartMapper;
import com.smartcart.cart_checkoutservice.models.Cart;
import com.smartcart.cart_checkoutservice.models.CartItem;
import com.smartcart.cart_checkoutservice.models.CartStatus;
import com.smartcart.cart_checkoutservice.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService{
    private CartRepository cartRepository;
    private CartMapper  cartMapper;
    private final ProductClient  productClient;
    private InventoryClient  inventoryClient;


    public CartServiceImpl(CartRepository cartRepository,CartMapper cartMapper,ProductClient productClient,InventoryClient inventoryClient) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
    }

    @Override
    public CartResponseDto addItem(Long userId, String userName, AddItemToCartRequest addItemToCartRequest) {
        if (addItemToCartRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        ApiResponse<VariantResponseDto> response=productClient.getVariantByVariantId(addItemToCartRequest.getVariantId());
        VariantResponseDto variant=response.getData();
        ApiResponse<ProductResponseDto> responseProduct=productClient.getSingleProduct(variant.getProductId());
        ProductResponseDto product=responseProduct.getData();
        InventoryResponseDto inventory=inventoryClient.checkStock(variant.getVariantId());

        //1. first find cart of user if present else create new one
        Cart cart=null;
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);
        if(cartOptional.isEmpty()){
            cart=new Cart();
            cart.setUserId(userId);
            cart.setUserName(userName);
            cart.setCartStatus(CartStatus.ACTIVE);
        }else cart=cartOptional.get();

        //2. Fetching CartItems from Cart and Check the item is adding is already exist or not
        Optional<CartItem> existingItem=cart.getCartItems()
                .stream()
                .filter(item ->item.getVariantId().equals(variant.getVariantId()))
                .findFirst();

        //3. If exist then add quantity else add item to cart
        if(existingItem.isPresent()){
            CartItem cartItem=existingItem.get();
            int updatedQuantity=cartItem.getQuantity()+addItemToCartRequest.getQuantity();
            if(inventory.getAvailableStock()<updatedQuantity){
                throw new RuntimeException("Not enough stock");
            }
            cartItem.setQuantity(cartItem.getQuantity()+addItemToCartRequest.getQuantity());
        }else{
            CartItem newItem = cartMapper.toEntity(addItemToCartRequest.getQuantity(),variant,product);
            cart.getCartItems().add(newItem);
        }

        // 4. Recalculate total
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getPriceAtAddition()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    public CartResponseDto getCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE)
                .orElseThrow(()->new RuntimeException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    public CartResponseDto updateItem(Long userId, Long variantId, Integer quantity) {
       if(quantity<0){
           throw new IllegalArgumentException("Quantity cannot be negative");
       }
       Cart cart = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE)
               .orElseThrow(()->new RuntimeException("Cart not found"));

       CartItem item=cart.getCartItems().stream()
               .filter(i ->i.getVariantId().equals(variantId))
               .findFirst()
               .orElseThrow(()->new RuntimeException("item not found"));

       if(quantity==0){
           cart.getCartItems().remove(item);
       }else{
           item.setQuantity(quantity);
       }

       BigDecimal total=cart.getCartItems().stream()
               .map(i->i.getPriceAtAddition()
                       .multiply(BigDecimal.valueOf(i.getQuantity())))
               .reduce(BigDecimal.ZERO, BigDecimal::add);

       cart.setTotalAmount(total);

       Cart savedCart=cartRepository.save(cart);
       return cartMapper.toDto(savedCart);
    }

    public void removeItem(Long userId,Long variantId) {
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);
        if(cartOptional.isEmpty()){
            throw new RuntimeException("Cart not found");
        }
        Cart cart=cartOptional.get();
        CartItem item=cart.getCartItems().stream()
                .filter(i->i.getVariantId().equals(variantId))
                .findFirst().orElseThrow(()->new RuntimeException("item not found"));

        cart.getCartItems().remove(item);
        cartRepository.save(cart);
    }

    public void clearCart(Long userId){
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);
        if(cartOptional.isEmpty()){
            throw new RuntimeException("Cart not found");
        }
        Cart cart=cartOptional.get();
        cart.setCartStatus(CartStatus.ABANDONED);
        cartRepository.save(cart);
    }

    public CartValidationResult validateCartInternal(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);
        if(cartOptional.isEmpty()){
            throw new RuntimeException("Cart not found");
        }
        System.out.println("Fetched user cart ");
        Cart cart=cartOptional.get();
        List<String> issues=new ArrayList<>();
        List<CartItem> validItems=new ArrayList<>();

        for(CartItem item:cart.getCartItems()){
            try{
                // 1. Product Service call

                ApiResponse<VariantResponseDto> response=productClient.getVariantByVariantId(item.getVariantId());
                VariantResponseDto variant=response.getData();
                if(variant==null){
                    issues.add("Item removed: product not found (variantId: " + item.getVariantId() + ")");
                    continue;
                }
                System.out.println("Product Service called!");
                // 2. Inventory Service call
                InventoryResponseDto inventory=inventoryClient.checkStock(item.getVariantId());

                System.out.println("Stock: " + inventory.getAvailableStock() +
                        " Required: " + item.getQuantity());
                if(inventory.getAvailableStock()<item.getQuantity()){
                    issues.add("Item removed: insufficient stock (variantId: " + item.getVariantId() + ")");
                    continue;
                }


                // 3. Price validation
                BigDecimal currentPrice=BigDecimal.valueOf(variant.getPrice());
                if(!item.getPriceAtAddition().equals(currentPrice)){
                    item.setPriceAtAddition(currentPrice);
                    issues.add("Price updated for variantId: " + item.getVariantId());
                }
                // 4. Keep valid item
                validItems.add(item);

            }catch (Exception e){
                e.printStackTrace();
                issues.add("Item removed due to error (variantId: " + item.getVariantId() + ")");
                throw e;
            }
        }
        // 5. Update cart items
        cart.setCartItems(validItems);

        // 6. Recalculate total
        BigDecimal total=cart.getCartItems().stream()
                .map(i->i.getPriceAtAddition()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);

        // 7. Save updated cart
        Cart updatedCart=cartRepository.save(cart);

        // 8. Prepare response
        CartValidationResult cartValidationResult=new CartValidationResult();
        cartValidationResult.setIssues(issues);
        cartValidationResult.setUpdatedcart(updatedCart);
        return cartValidationResult;
    }

    @Override
    public CartValidationResponse validateCart(Long userId) {
        CartValidationResult result=validateCartInternal(userId);

        CartValidationResponse response=new CartValidationResponse();
        response.setValid(result.getIssues().isEmpty());
        response.setIssues(result.getIssues());
        response.setUpdatedCart(cartMapper.toDto(result.getUpdatedcart()));
        return response;
    }

    @Override
    public CartSummaryDto getCartSummary(Long userId) {
        Optional<Cart> cartOptional = cartRepository.findByUserIdAndCartStatus(userId,CartStatus.ACTIVE);
        if(cartOptional.isEmpty()){
            throw new RuntimeException("Cart not found");
        }
        Cart cart=cartOptional.get();
        Integer quantity=cart.getCartItems().stream()
                .map(i->i.getQuantity())
                .reduce(0,Integer::sum);

        CartSummaryDto summary=new CartSummaryDto();
        summary.setCartId(cart.getCartId());
        summary.setTotalAmount(cart.getTotalAmount());
        summary.setTotalItems(cart.getCartItems().size());
        summary.setTotalQuantity(quantity);
        return summary;
    }
}
