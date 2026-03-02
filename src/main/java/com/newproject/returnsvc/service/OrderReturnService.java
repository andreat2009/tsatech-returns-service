package com.newproject.returnsvc.service;

import com.newproject.returnsvc.domain.OrderReturn;
import com.newproject.returnsvc.dto.OrderReturnRequest;
import com.newproject.returnsvc.dto.OrderReturnResponse;
import com.newproject.returnsvc.events.EventPublisher;
import com.newproject.returnsvc.exception.NotFoundException;
import com.newproject.returnsvc.repository.OrderReturnRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderReturnService {
    private final OrderReturnRepository orderReturnRepository;
    private final EventPublisher eventPublisher;

    public OrderReturnService(OrderReturnRepository orderReturnRepository, EventPublisher eventPublisher) {
        this.orderReturnRepository = orderReturnRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public OrderReturnResponse create(OrderReturnRequest request) {
        OrderReturn orderReturn = new OrderReturn();
        orderReturn.setOrderId(request.getOrderId());
        orderReturn.setOrderItemId(request.getOrderItemId());
        orderReturn.setCustomerId(request.getCustomerId());
        orderReturn.setReason(request.getReason());
        orderReturn.setComment(request.getComment());
        orderReturn.setStatus("REQUESTED");
        OffsetDateTime now = OffsetDateTime.now();
        orderReturn.setCreatedAt(now);
        orderReturn.setUpdatedAt(now);

        OrderReturn saved = orderReturnRepository.save(orderReturn);
        eventPublisher.publish("ORDER_RETURN_CREATED", "order_return", saved.getId().toString(), toResponse(saved));
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderReturnResponse> list(Long customerId, Long orderId) {
        List<OrderReturn> returns;
        if (orderId != null) {
            returns = orderReturnRepository.findByOrderIdOrderByCreatedAtDesc(orderId);
        } else if (customerId != null) {
            returns = orderReturnRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
        } else {
            returns = orderReturnRepository.findAll();
        }
        return returns.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public OrderReturnResponse setStatus(Long id, String status) {
        OrderReturn orderReturn = orderReturnRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Order return not found"));

        orderReturn.setStatus(status);
        orderReturn.setUpdatedAt(OffsetDateTime.now());
        OrderReturn saved = orderReturnRepository.save(orderReturn);
        eventPublisher.publish("ORDER_RETURN_UPDATED", "order_return", saved.getId().toString(), toResponse(saved));
        return toResponse(saved);
    }

    private OrderReturnResponse toResponse(OrderReturn orderReturn) {
        OrderReturnResponse response = new OrderReturnResponse();
        response.setId(orderReturn.getId());
        response.setOrderId(orderReturn.getOrderId());
        response.setOrderItemId(orderReturn.getOrderItemId());
        response.setCustomerId(orderReturn.getCustomerId());
        response.setReason(orderReturn.getReason());
        response.setComment(orderReturn.getComment());
        response.setStatus(orderReturn.getStatus());
        response.setCreatedAt(orderReturn.getCreatedAt());
        response.setUpdatedAt(orderReturn.getUpdatedAt());
        return response;
    }
}
