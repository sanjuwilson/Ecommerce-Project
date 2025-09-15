package com.app.customer_service.producer;


import com.ecom.EmailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, EmailRequest> templateRequest;
    public void sendEmailRequest(EmailRequest emailRequest) {
        Message<EmailRequest> message = MessageBuilder
                .withPayload(emailRequest)
                .setHeader(KafkaHeaders.TOPIC, "admin_msg_topic")
                .build();
        templateRequest.send(message);
        System.out.println("sendMessage");
    }
}