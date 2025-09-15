package com.app.payment.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.internals.Topic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {
    private final KafkaTemplate<String,PaymentNotificationRequest> kfkafkaTemplate;


    public void sendNotification(PaymentNotificationRequest notification) {
        log.info("Sending notification with bod<{}>",notification);
        Message<PaymentNotificationRequest> message = MessageBuilder.withPayload(notification)
                .setHeader(TOPIC, "payment-topic").build();
        kfkafkaTemplate.send(message);

    }
}
