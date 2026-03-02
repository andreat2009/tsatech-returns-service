package com.newproject.returnsvc.controller;

import com.newproject.returnsvc.dto.OrderReturnRequest;
import com.newproject.returnsvc.dto.OrderReturnResponse;
import com.newproject.returnsvc.service.OrderReturnService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/returns")
public class OrderReturnController {
    private final OrderReturnService orderReturnService;

    public OrderReturnController(OrderReturnService orderReturnService) {
        this.orderReturnService = orderReturnService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderReturnResponse create(@Valid @RequestBody OrderReturnRequest request) {
        return orderReturnService.create(request);
    }

    @GetMapping
    public List<OrderReturnResponse> list(
        @RequestParam(required = false) Long customerId,
        @RequestParam(required = false) Long orderId
    ) {
        return orderReturnService.list(customerId, orderId);
    }

    @PatchMapping("/{id}/status")
    public OrderReturnResponse setStatus(@PathVariable Long id, @RequestParam String status) {
        return orderReturnService.setStatus(id, status);
    }
}
