package com.app.cart.kafka_consumer;

import com.app.cart.service_cart.Cart;
import com.app.cart.service_cart.CartService;
import com.ecom.CartUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class CartConsumer {
    private final CartService cartService;
    @KafkaListener(topics = "cart_topic",groupId = "cartGroup")
    public void receiveOrderConfirmation(CartUpdater cartUpdater){
        cartService.updateCart(cartUpdater);

    }
   
}
