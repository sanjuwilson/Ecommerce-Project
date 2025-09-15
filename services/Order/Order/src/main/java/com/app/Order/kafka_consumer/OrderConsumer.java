package com.app.Order.kafka_consumer;
import com.app.Order.service_order.OrderService;
import com.ecom.OrderUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {
    private final OrderService orderService;
    @KafkaListener(topics = "order_topic",groupId = "orderGroup")
    public void receiveOrderConfirmation(OrderUpdater orderUpdater){
        orderService.updateOrder(orderUpdater);

    }

}
