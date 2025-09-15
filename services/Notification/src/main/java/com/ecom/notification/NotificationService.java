package com.ecom.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    public Integer save(Notification notification) {
        var confirmation = notification.getOrderConfirmation();

        if (confirmation != null) {
            System.out.println("Payment methods in confirmation: " + confirmation.getPaymentMethod());// 🔧 Defensive Null Check
            if (confirmation.getPaymentMethod() == null) {
                confirmation.setPaymentMethod(new ArrayList<>());
            }

            confirmation.setNotification(notification);

            if (confirmation.getCustomer() != null) {
                confirmation.getCustomer().setOrderConfirmation(confirmation);
            }
            if (confirmation.getProducts() != null) {
                confirmation.getProducts().forEach(p -> p.setOrderConfirmation(confirmation));
            }
        }
        if (notification.getPaymentConfirmation() != null) {
            notification.getPaymentConfirmation().setNotification(notification);
        }
        if (notification.getPointDetailsConfirmation() != null) {
            notification.getPointDetailsConfirmation().setNotification(notification);
        }
        return notificationRepository.save(notification).getId();
    }

}
