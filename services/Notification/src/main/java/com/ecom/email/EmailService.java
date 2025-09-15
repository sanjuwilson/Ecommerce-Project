package com.ecom.email;

import com.ecom.ProductPointsRecord;
import com.ecom.TransactionMethod;
import com.ecom.order.Product;
import com.ecom.payment.PaymentConfirmation;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecom.email.EmailTemplate.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(String destination, String customerName, BigDecimal amount, String orderReference, Map<TransactionMethod,BigDecimal>methodAndAmount) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom("sanjuw321@gmail.com");
        final String templateName= PAYMENT_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("amount",amount);
        variables.put("orderReference",orderReference);
        variables.put("transactionMethodsAndAmount",methodAndAmount);
        Context context = new Context();
        context.setVariables(variables);
        mimeMessageHelper.setSubject(PAYMENT_CONFIRMATION.getSubject());
        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate,true);
            mimeMessageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info("Email sent");
        }catch (Exception e){
            log.warn("Cannot send email to {}",destination);
        }

    }
    @Async
    public void sendOrderConfirmationEmail(String destination, String customerName, BigDecimal amount, String orderReference, List<Product>products,List<TransactionMethod>transactionMethods) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom("sanjuw321@gmail.com");
        final String templateName= ORDER_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("totalAmount",amount);
        variables.put("orderReference",orderReference);
        variables.put("products",products);
        variables.put("transactionMethods",transactionMethods);
        Context context = new Context();
        context.setVariables(variables);
        mimeMessageHelper.setSubject(ORDER_CONFIRMATION.getSubject());
        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate,true);
            mimeMessageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info("Email sent");
        }catch (Exception e){
            log.warn("Cannot send email to {}",destination);
        }

    }
    @Async
    public void sendPointDetailsConfirmationEmail(int totalPoints,String customerName,
int pointsEarned,String destination, double equivalentCashEarned,List<ProductPointsRecord>pointsRecords) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());
        mimeMessageHelper.setFrom("sanjuw321@gmail.com");
        final String templateName= POINT_DETAILS_CONFIRMATION.getTemplate();
        Map<String,Object> variables = new HashMap<>();
        variables.put("customerName",customerName);
        variables.put("totalPoints",totalPoints);
        variables.put("equivalentCashEarned",equivalentCashEarned);
        variables.put("PointsEarned",pointsEarned);
        variables.put("pointsRecords",pointsRecords);
        Context context = new Context();
        context.setVariables(variables);
        mimeMessageHelper.setSubject( POINT_DETAILS_CONFIRMATION.getSubject());
        try{
            String htmlTemplate = templateEngine.process(templateName, context);
            mimeMessageHelper.setText(htmlTemplate,true);
            mimeMessageHelper.setTo(destination);
            mailSender.send(mimeMessage);
            log.info("Email sent");
        }catch (Exception e){
            log.warn("Cannot send email to {}",destination);
        }

    }
    @Async
    public void sendAdminVerificationLink(String fullName, String email, String url) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom("sanjuwilson15@gmail.com");
            helper.setTo(email);
            helper.setSubject(ADMIN_VERIFICATION.getSubject());

            Map<String, Object> variables = new HashMap<>();
            variables.put("adminName", fullName);
            variables.put("url", url);

            Context context = new Context();
            context.setVariables(variables);

            String htmlTemplate = templateEngine.process(ADMIN_VERIFICATION.getTemplate(), context);
            helper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", email);
        } catch (Exception e) {
            log.warn("Cannot send email to {}: {}", email, e.getMessage(), e);
        }
    }

}
