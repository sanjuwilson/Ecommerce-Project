package com.ecom;

public record EmailRequest(
       SuperAdminRequest superAdminRequest,
       String url
) {
}
