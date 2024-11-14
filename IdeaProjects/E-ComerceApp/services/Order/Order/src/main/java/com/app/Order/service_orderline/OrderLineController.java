package com.app.Order.service_orderline;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order-line-controller")
@RequiredArgsConstructor
public class OrderLineController {
    private final OrderLineService orderLineService;
    @PostMapping
    public OrderLine save(@RequestBody OrderLineRequest orderLineRequest) {
        return orderLineService.save(orderLineRequest);

    }
    @GetMapping
    public ResponseEntity<List<OrderLineResponse>> findAll() {
        return ResponseEntity.ok(orderLineService.getAll());
    }
    @GetMapping("{/id}")
    public ResponseEntity<OrderLineResponse> findById(@PathVariable("id") int Id) {
        return ResponseEntity.ok(orderLineService.findById(Id));
    }

}
