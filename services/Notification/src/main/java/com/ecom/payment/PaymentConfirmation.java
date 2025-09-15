package com.ecom.payment;

import com.ecom.TransactionMethod;
import com.ecom.notification.Notification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentConfirmation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderReference;
    private BigDecimal orderAmount;
    @ElementCollection(targetClass = TransactionMethod.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "payment_methods_confirmation",
            joinColumns = @JoinColumn(name = "payment_record_id")
    )
    @Column(name = "payment_method")
    private List<TransactionMethod> paymentMethods=new ArrayList<>();
    String firstName;
    String lastName;
    String email;
    @OneToOne
    @JoinColumn(name="notification_id")
    Notification notification;


}
