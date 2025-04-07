package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.OrderStatus;
import com.ecommerce.order.exception.ServiceException;
import com.ecommerce.order.exception.ServiceExceptionCode;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import javax.annotation.PostConstruct;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderKafkaProducer orderKafkaProducer;
    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;

    public OrderResponse createOrder(OrderRequest request) {
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            Order order = buildOrder(request);
            Order orderEntity  = orderRepository.save(order);
            OrderCreatedEvent event = buildOrderCreatedEvent(orderEntity);
            orderKafkaProducer.sendOrderCreatedEvent(event);

            meterRegistry.counter("order_created_total").increment();
            sample.stop(meterRegistry.timer("order_processing_time"));

            return buildOrderResponse(orderEntity);
        } catch (Exception e) {
            meterRegistry.counter("order_failed_total").increment();
            log.error("Order creation failed: {}", e.getMessage(), e);
            throw new ServiceException(ServiceExceptionCode.FAIL_CREATE_ORDER);
        }
    }



    public OrderResponse getOrderById(Long id) {
        Timer.Sample sample = Timer.start(meterRegistry);

        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));

        sample.stop(meterRegistry.timer("order_processing_time"));

        return buildOrderResponse(order);
    }

    private Order buildOrder(OrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setShoppingAddress(request.getShoppingAddress());
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus(request.getStatus());
        return order;
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
