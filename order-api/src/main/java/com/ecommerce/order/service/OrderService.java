package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.repository.OrderRepository;
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
    private final MeterRegistry meterRegistry;
    private Counter orderCounter;

    /*OrderService 객체를 생성한 후, 이 메서드를 실행*/
    @PostConstruct
    private void initMetrics() {
        this.orderCounter = Counter.builder("order.created.count")
                .description("Number of created orders")
                .register(meterRegistry);
    }

    public Order createOrder(Order order) {
        Order savedOrder  = orderRepository.save(order);
        orderCounter.increment();

        OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getUserId(), savedOrder.getProductId(), savedOrder.getQuantity());
        orderKafkaProducer.sendOrderCreatedEvent(event);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
