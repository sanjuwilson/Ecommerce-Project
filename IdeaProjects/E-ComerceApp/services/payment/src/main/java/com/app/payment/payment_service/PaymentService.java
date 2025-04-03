package com.app.payment.payment_service;

import com.app.payment.notification.NotificationProducer;
import com.app.payment.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final NotificationProducer notificationProducer;

    public Integer save(PaymentRequest request) {
        Payment pay= paymentRepository.save(mapper.toPayment(request));
        notificationProducer.sendNotification(new PaymentNotificationRequest(
                request.orderReference(),
                request.amount(),
                request.method(),
                request.customer().firstName(),
                request.customer().lastName(),
                request.customer().email()

        ));
        return pay.getId();
    }
}
