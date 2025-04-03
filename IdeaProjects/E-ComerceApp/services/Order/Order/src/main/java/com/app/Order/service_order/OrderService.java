package com.app.Order.service_order;

import com.app.Order.exceptions.BuissnessException;
import com.app.Order.kafka.OrderConfirmation;
import com.app.Order.kafka.OrderProducer;
import com.app.Order.payment.PaymentClient;
import com.app.Order.payment.PaymentRequest;
import com.app.Order.service_customer.CustomerClient;
import com.app.Order.service_customer.CustomerResponse;
import com.app.Order.service_orderline.OrderLineRequest;
import com.app.Order.service_orderline.OrderLineService;
import com.app.Order.service_product.ProductClient;
import com.app.Order.service_product.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient client;
    private final ProductClient proClient;
    private final OrderRepository repo;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer placeOrder(OrderRequest request) {
        CustomerResponse customerResponse = this.client.findById(request.customerId())
                .orElseThrow(() -> new BuissnessException("No Customer Found"));
        var purchasedProducts=proClient.requestOrder(request.purchase());
        Order order=repo.save(mapper.toOrder(request));
        for(PurchaseRequest purchaseRequest:request.purchase()){
            orderLineService.save(new OrderLineRequest(
                    null,
                    order.getId(),
                    purchaseRequest.id(),
                    purchaseRequest.quantity()
            ));
        }
        paymentClient.requestPayment(new PaymentRequest(
                request.price(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customerResponse
        ));
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.price(),
                        request.paymentMethod(),
                        customerResponse,
                        purchasedProducts
                )
        );
        return order.getId();
    }


    public List<OrderResponse> findAll() {
        return repo.findAll().stream().map(mapper ::toOrderResponse).collect(Collectors.toList());
    }

    public OrderResponse getById(int orderId) {
        return repo.findById(orderId).map(mapper::toOrderResponse).orElseThrow(()->new EntityNotFoundException("No Order Found"));
    }
    public void deleteById(int orderId) {
        repo.deleteById(orderId);
    }
}
