package com.app.Order.service_order;

import com.app.Order.exceptions.BuissnessException;
import com.app.Order.payment.PaymentClient;
import com.app.Order.service_customer.CustomerClient;
import com.app.Order.service_customer.CustomerResponse;
import com.app.Order.service_orderline.OrderLine;
import com.app.Order.service_orderline.OrderLineService;
import com.app.Order.service_product.ProductClient;
import com.app.Order.service_product.ProductResponse;
import com.app.Order.securtiy.TokenGenerator;
import com.app.Order.service_transaction.ProductResponseTransaction;
import com.app.Order.service_transaction.TransactionResponse;
import com.ecom.OrderStatus;
import com.ecom.OrderUpdater;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CustomerClient client;
    private final ProductClient proClient;
    private final OrderRepository repo;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final PaymentClient paymentClient;
    private static ProductResponse productResponse;
    private final TokenGenerator tokenGenerator;

    public Integer placeOrder(OrderRequest request,Integer customerId) {
        Order orderExisting =repo.findByCartReference(request.cartReference());
        BigDecimal total = BigDecimal.ZERO;
        String token = "Bearer " + tokenGenerator.generateToken();
        var purchasedProducts=proClient.requestOrder(request.purchase(),token);
        findCustomerDetailsByCustomerId(customerId);



        Map<Integer, Double> quantityByProductId = request.purchase().stream()
                .collect(Collectors.toMap(PurchaseRequest::id, PurchaseRequest::quantity));
        Map<Integer, ProductResponse> productById = purchasedProducts.stream()
                .collect(Collectors.toMap(ProductResponse::id, p -> p));
        for (ProductResponse product : purchasedProducts) {
            double quantity = quantityByProductId.getOrDefault(product.id(), 0.0); // match by ID
            if (product.price() != null) {
                BigDecimal itemTotal = product.price().multiply(BigDecimal.valueOf(quantity));
                total = total.add(itemTotal);
            }
        }
        var purchases=request.purchase();
        Order order;
        if(orderExisting!=null) {
            orderExisting.setCustomerId(customerId);
            orderExisting.setPrice(total);
            orderExisting.setOrderStatus(OrderStatus.QUEUED);
            orderExisting.setUpdatedAt(LocalDateTime.now());
            orderExisting.getOrderLines().clear();
            List<OrderLine> updatedLines = new ArrayList<>();
            for (PurchaseRequest purchase : purchases) {
                ProductResponse product = productById.get(purchase.id());
                if (product != null) {
                    updatedLines.add(OrderLine.builder()
                            .order(orderExisting)
                            .productId(purchase.id())
                            .quantity(purchase.quantity())
                            .category(product.description())
                            .price(product.price())
                            .name(product.name())
                            .build());
                }
            }
            orderExisting.setOrderLines(updatedLines);
            order = repo.save(orderExisting);

        }

        else{
            order = Order.builder()
                    .customerId(customerId)
                    .price(total)
                    .reference(OrderReferenceGenerator.generateOrderReference())
                    .cartReference((request.cartReference()!=null)?request.cartReference():null)
                    .orderStatus(OrderStatus.QUEUED)
                    .build();

            order = repo.save(order); // Save initially to get order ID

            List<OrderLine> orderLines = new ArrayList<>();
            for (PurchaseRequest purchase : purchases) {
                ProductResponse product = productById.get(purchase.id());
                if (product != null) {
                    orderLines.add(OrderLine.builder()
                            .order(order)
                            .productId(purchase.id())
                            .quantity(purchase.quantity())
                            .category(product.description())
                            .price(product.price())
                            .name(product.name())
                            .build());
                }
            }

            order.setOrderLines(orderLines);
            repo.save(order); // Save again to update with order lines
        }

        return order.getId();
    }


    public List<OrderResponse> findAll() {
        return repo.findAll().stream().map(mapper ::toOrderResponse).collect(Collectors.toList());
    }

    public OrderResponse getById(int orderId) {
        return repo.findById(orderId).map(mapper::toOrderResponse).orElseThrow(()->new EntityNotFoundException("No Order Found"));
    }
    public void deleteById(String reference) {
        repo.deleteOrderByReference(reference);
    }
    public TransactionResponse getByReference(String reference) {
        Order order=findByReference(reference);
        CustomerResponse response= findCustomerDetailsByCustomerId(order.getCustomerId());
        return new TransactionResponse(response,order.getPrice(),order.getCustomerId(),order.getOrderLines().stream().map(orderLine-> new ProductResponseTransaction(orderLine.getProductId(),orderLine.getQuantity(),orderLine.getCategory(),orderLine.getName(),orderLine.getPrice())).collect(Collectors.toList()),order.getCartReference());
    }
    public void updateOrder(OrderUpdater updater){
        Order order=findByReference(updater.reference());
        order.setUpdatedAt(updater.now());
        order.setOrderStatus(updater.orderStatus());
        repo.save(order);


    }
    private Order findByReference(String reference) {
        return repo.findOrderByReference(reference);

    }
    private CustomerResponse findCustomerDetailsByCustomerId(Integer id){
        String token = "Bearer " + tokenGenerator.generateToken();
        return this.client.findById(id,token) .orElseThrow(() -> new BuissnessException("No Customer Found"));

    }
}
