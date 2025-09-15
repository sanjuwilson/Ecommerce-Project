package com.ecom.email;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    PAYMENT_CONFIRMATION("payment_confirmation.html","Payment Sucessfully processed"),
    ORDER_CONFIRMATION("order_confirmation.html","Order confirmation"),
    POINT_DETAILS_CONFIRMATION("point.html","Point details confirmation"),
    ADMIN_VERIFICATION("admin_verification.html","Admin verification");

    private final String template;
    private final String subject;


    EmailTemplate(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }

}
