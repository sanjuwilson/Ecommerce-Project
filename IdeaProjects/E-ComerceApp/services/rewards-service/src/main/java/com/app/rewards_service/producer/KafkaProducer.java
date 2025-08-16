package com.app.rewards_service.producer;

import com.ecom.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String,PointDetailsConfirmationRecord> templatePoints;
    private final KafkaTemplate<String, OrderUpdater> templateOrderUpdater;
    private final KafkaTemplate<String, CartUpdater> templateCartUpdater;
    private final KafkaTemplate<String, OrderConfirmationRecord> templateOrderConfirmation;
    private final KafkaTemplate<String, PaymentConfirmationRecord> templatePaymentConfirmation;

    public void sendPointsMessage(PointDetailsConfirmationRecord pointDetailsConfirmation) {
        Message<PointDetailsConfirmationRecord> message= MessageBuilder
                .withPayload(pointDetailsConfirmation)
                .setHeader(KafkaHeaders.TOPIC,"points_topic")
                .build();
        templatePoints.send(message);
        System.out.println("sendPointsMessage");


    }
    public void sendOrderMessage(OrderConfirmationRecord orderDetailsConfirmation) {
        Message<OrderConfirmationRecord> message= MessageBuilder
                .withPayload(orderDetailsConfirmation)
                .setHeader(KafkaHeaders.TOPIC,"order_topic_con")
                .build();
        templateOrderConfirmation.send(message);
        System.out.println("sendOrderMessage");


    }
    public void sendPaymentMessage(PaymentConfirmationRecord paymentDetailsConfirmation) {
        Message<PaymentConfirmationRecord> message= MessageBuilder
                .withPayload(paymentDetailsConfirmation)
                .setHeader(KafkaHeaders.TOPIC,"payment_topic")
                .build();
        templatePaymentConfirmation.send(message);
        System.out.println("sendPaymentsMessage");


    }
    public void sendOrderStatusUpdate(OrderUpdater orderUpdater) {
        Message<OrderUpdater> message= MessageBuilder
                .withPayload(orderUpdater)
                .setHeader(KafkaHeaders.TOPIC,"order_topic")
                .build();
        templateOrderUpdater.send(message);
        System.out.println("sendOrderStatusMessage");
    }
    public void sendCartStatusUpdate(CartUpdater cartUpdater) {
        Message<CartUpdater> message= MessageBuilder
                .withPayload(cartUpdater)
                .setHeader(KafkaHeaders.TOPIC,"cart_topic")
                .build();
        templateCartUpdater.send(message);
        System.out.println("sendCartMessage");
    }

}
