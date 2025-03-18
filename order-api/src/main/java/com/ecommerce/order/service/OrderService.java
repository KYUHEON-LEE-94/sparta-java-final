package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.OrderStatus;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(Order request) {
        Order order  = orderRepository.save(request);


        OrderCreatedEvent event = buildOrderCreatedEvent(order);
        orderKafkaProducer.sendOrderCreatedEvent(event);

        return buildOrderResponse(order);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        return buildOrderResponse(order);
    }

    private OrderResponse buildOrderResponse(Order order) {
        return OrderResponse.builder()
            .orderId(order.getId())
            .userId(order.getUserId())
            .productId(order.getProductId())
            .shoppingAddress(order.getShoppingAddress())
            .quantity(order.getQuantity())
            .totalPrice(order.getTotalPrice())
            .status(order.getStatus())
            .createdAt(order.getCreatedAt())
            .build();
    }

    private OrderCreatedEvent buildOrderCreatedEvent(Order order) {
      return OrderCreatedEvent.builder()
          .orderId(order.getId())
          .userId(order.getUserId())
          .productId(order.getProductId())
          .quantity(order.getQuantity())
          .build();
    }
}
