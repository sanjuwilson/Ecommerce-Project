package com.app.cart.service_cart;

import com.app.cart.exceptions.CartAlreadyActiveException;
import com.app.cart.exceptions.CartAlreadyCheckedOutException;
import com.app.cart.exceptions.CartNotActiveException;
import com.app.cart.service_customer.CustomerClient;
import com.app.cart.service_details.ProductDetails;
import com.app.cart.service_details.ProductDetailsRequest;
import com.app.cart.service_order.OrderClient;
import com.app.cart.service_order.OrderRequest;
import com.app.cart.service_product.ProductClient;
import com.app.cart.service_product.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final CustomerClient customerClient;


    public Integer saveCart(CartRequest cartRequest) throws CartAlreadyActiveException {
        if(checkForCartEligibility(cartRequest)) {
            Cart cart = Cart.builder()
                    .userId(cartRequest.userId())
                    .status(cartRequest.status())
                    .build();

            List<ProductDetails> details = cartRequest.detailsRequests().stream()
                    .map(request -> ProductDetails.builder()
                            .productId(request.productId())
                            .quantity(request.quantity())
                            .cart(cart)
                            .build())
                    .toList();
            List<ProductDetails> detailsNew=details.stream().filter(this::checkForAvailability).toList();
            cart.setDetails(detailsNew);
            return cartRepository.save(cart).getId();
        }
        else{
           return null;
        }
    }
    @Transactional
    public Integer addToCart(List<ProductDetailsRequest> productDetailsRequest,Integer cartId) throws CartNotActiveException {
       Cart existingCart=cartRepository.findById(cartId).orElseThrow(()->new EntityNotFoundException("Cart not found"));
       List<ProductDetails> existingDetails=existingCart.getDetails();
       if(existingCart.getStatus().equals(CartStatus.ACTIVE)){
           for(ProductDetailsRequest productDetailRequest:productDetailsRequest){
               if(checkForAvailability(ProductDetails.builder()
                       .productId(productDetailRequest.productId())
                       .quantity(productDetailRequest.quantity())
                       .cart(existingCart)
                       .build())){
                   boolean similar=false;
                   for(ProductDetails productDetails:existingDetails){
                       if(productDetailRequest.productId()==productDetails.getProductId()){
                           similar=true;
                           productDetails.setQuantity(productDetails.getQuantity()+productDetailRequest.quantity());
                           break;
                       }
                   }
                   if(!similar){
                       ProductDetails newDetail=ProductDetails.builder().productId(productDetailRequest.productId())
                               .quantity(productDetailRequest.quantity())
                               .cart(existingCart) // Associate with the cart
                               .build();
                       existingDetails.add(newDetail);
                   }

               }

           }
           existingCart.setDetails(existingDetails);
           return cartRepository.save(existingCart).getId();
       }
       else{
           throw new CartNotActiveException("Cart Not Active");
       }
    }
    private boolean checkForAvailability(ProductDetails productDetails) {
        ProductResponse response=productClient.getById(productDetails.getProductId());
        return response.availableQuantity()>=productDetails.getQuantity();
    }
    @Transactional
    public void checkOutCart(Integer cartId,CheckOutRequest checkoutRequest) throws CartAlreadyCheckedOutException {
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new EntityNotFoundException("Cart not found"));
        if(cart.getStatus().equals(CartStatus.ACTIVE)){
            orderClient.placeOrder(new OrderRequest(checkoutRequest.reference(),checkoutRequest.price()
                    ,checkoutRequest.paymentMethod(),cart.getUserId(),
                    cart.details.stream().map(cartMapper::toPurchaseRequest).collect(Collectors.toList())));
            cart.setStatus(CartStatus.CHECKED_OUT);
            cartRepository.save(cart);
        }
        else{
            throw new CartAlreadyCheckedOutException("No Active Carts");
        }

    }
    private boolean checkForCartEligibility(CartRequest cartRequest) throws CartAlreadyActiveException {
        boolean isCustomer = customerClient.getCustomer(cartRequest.userId());
        if (!isCustomer) {
            throw new EntityNotFoundException("Customer not found");
        }
        else{
            int activeCount = cartRepository.countByUserIdAndStatus(cartRequest.userId(), CartStatus.ACTIVE);
            if(activeCount>0){
                throw new CartAlreadyActiveException("Cart Already Active");
            }
            return activeCount == 0;
        }

    }

    public void deleteProductsFromCart(List<ProductDetailsRequest> deleteRequest, Integer cartId) {
        boolean found=false;
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new EntityNotFoundException("Cart not found"));
        List<ProductDetails> existingProducts=cart.getDetails();
        Iterator<ProductDetails> iterator=existingProducts.iterator();
        Map<Integer, Double> productMapper = deleteRequest.stream()
                .collect(Collectors.toMap(ProductDetailsRequest::productId, ProductDetailsRequest::quantity));
        while(iterator.hasNext()){
            ProductDetails currentProduct=iterator.next();
            if (productMapper.containsKey(currentProduct.getProductId())) {
                double deleteQuantity=productMapper.get(currentProduct.getProductId());

                if (deleteQuantity<=currentProduct.getQuantity()) {
                    currentProduct.setQuantity(currentProduct.getQuantity()-deleteQuantity);
                    found=true;
                    if (currentProduct.getQuantity()==0) {
                        iterator.remove();
                    }
                } else {
                    throw new RuntimeException("Given Quantity exceeds existing Quantity for Product ID: "
                            + currentProduct.getProductId());
                }
            }
        }

        if(!found){
            throw new RuntimeException("No Product Found with the Given ID");

        }
        if(existingProducts.isEmpty()){
            cart.setStatus(CartStatus.DELETED);
        }
        cartRepository.save(cart);

    }
}
