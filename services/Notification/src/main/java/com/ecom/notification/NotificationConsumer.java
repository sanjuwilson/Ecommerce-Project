package com.ecom.notification;

import com.ecom.*;
import com.ecom.email.EmailService;
import com.ecom.order.Customer;
import com.ecom.order.OrderConfirmation;
import com.ecom.order.Product;
import com.ecom.payment.PaymentConfirmation;
import com.ecom.point.PointDetailsConfirmation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final EmailService emailService;
    @KafkaListener(topics = "points_topic",groupId = "myGroup-NEW")
    public void consumePointDetailsNotification(PointDetailsConfirmationRecord confirmationRecord) throws MessagingException {
        log.info("Received point details notification: " + confirmationRecord);
        var notification=Notification.builder().notificationType(NotificationType.POINT_DETAILS_CONFIRMATION)
                .pointDetailsConfirmation(PointDetailsConfirmation.builder()
                        .pointsEarned(confirmationRecord.pointsEarned())
                        .totalPoints(confirmationRecord.totalPoints())
                        .customerName(confirmationRecord.customerName())
                        .customerEmail(confirmationRecord.email())
                        .orderReference(confirmationRecord.orderReference())
                        .equivalentCashEarned(confirmationRecord.equivalentCashEarned()).build()).build();
        notificationService.save(notification);
        emailService.sendPointDetailsConfirmationEmail(
                confirmationRecord.totalPoints(), confirmationRecord.customerName(), confirmationRecord.pointsEarned(), confirmationRecord.email(), confirmationRecord.equivalentCashEarned(),confirmationRecord.productPointsRecordList()
        );

    }
    @KafkaListener(topics = "payment_topic",groupId = "myGroup-NEW")
    public void consumePaymentNotification(PaymentConfirmationRecord paymentConfirmation) throws MessagingException {
        List<TransactionMethod> methods = new ArrayList<>(paymentConfirmation.paymentMethods().keySet());
        log.info("Received payment details notification: " + paymentConfirmation);
        var notification=Notification.builder().notificationType(NotificationType.PAYMENT_CONFIRMATION)
                        .paymentConfirmation(PaymentConfirmation.builder()
                                .email(paymentConfirmation.email())
                                .firstName(paymentConfirmation.firstName())
                        .lastName(paymentConfirmation.lastName())
                                .orderReference(paymentConfirmation.orderReference())
                                        .orderAmount(paymentConfirmation.orderAmount())
                                                .paymentMethods(methods).build()).build();

        notificationService.save(notification);
        String name=paymentConfirmation.firstName()+" "+paymentConfirmation.lastName();
        emailService.sendPaymentSuccessEmail(paymentConfirmation.email(), name,paymentConfirmation.orderAmount(), paymentConfirmation.orderReference(),paymentConfirmation.paymentMethods());

    }
    @KafkaListener(topics = "order_topic_con",groupId = "myGroup-NEW")
    public void consumeOrderNotification(OrderConfirmationRecord orderConfirmation) throws MessagingException {
        System.out.println("Received order confirmation: " + orderConfirmation);
        log.info("Received order details notification: " + orderConfirmation);
        List<Product>products=orderConfirmation.products().stream().map(productRecord -> Product.builder().price(productRecord.price())
               .quantity(productRecord.quantity())
               .name(productRecord.name())
                .description(productRecord.description())
               .build()).toList();
        var notification=Notification.builder().notificationType(NotificationType.ORDER_CONFIRMATION)
                .orderConfirmation(OrderConfirmation.builder()
                        .totalAmount(orderConfirmation.totalAmount())
                        .orderReference(orderConfirmation.orderReference())
                        .paymentMethod(orderConfirmation.paymentMethods())
                        .customer(Customer.builder().firstName(orderConfirmation.customer().firstName())
                                        .lastName(orderConfirmation.customer().lastName())
                                        .email(orderConfirmation.customer().email())
                                        .build())
                        .products(products)
                       .build()).build();

        notificationService.save(notification);
        String fullName=orderConfirmation.customer().firstName()+" "+orderConfirmation.customer().lastName();
        emailService.sendOrderConfirmationEmail(orderConfirmation.customer().email(),fullName,orderConfirmation.totalAmount(), orderConfirmation.orderReference(), products,orderConfirmation.paymentMethods());

    }
    @KafkaListener(topics = "admin_msg_topic",groupId = "myGroup-NEW")
    public void consumeEmailRequest(EmailRequest emailRequest) throws MessagingException {
        log.info("Received point details notification: " + emailRequest);
        String fullName=emailRequest.superAdminRequest().firstName()+" "+emailRequest.superAdminRequest().lastName();
        emailService.sendAdminVerificationLink(fullName,emailRequest.superAdminRequest().Email(),emailRequest.url());

    }



}
