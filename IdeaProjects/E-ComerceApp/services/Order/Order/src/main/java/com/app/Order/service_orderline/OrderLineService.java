package com.app.Order.service_orderline;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineService {
    private final OrderLineRepository repo;
    private final OrderLineMapper mapper;
    public OrderLine save(OrderLineRequest request){
        OrderLine line = mapper.toOrderLine(request);
        return repo.save(line);
    }

    public List<OrderLineResponse> getAll() {
        return repo.findAll().stream().map(mapper ::toOrderLineResponse).collect(Collectors.toList());
    }
    public OrderLineResponse findById(int id){
        return repo.findById(id).map(mapper ::toOrderLineResponse).orElseThrow(()->new EntityNotFoundException("Order line not found"));
    }
}
