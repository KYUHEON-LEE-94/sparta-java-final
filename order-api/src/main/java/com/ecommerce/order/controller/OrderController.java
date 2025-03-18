package com.ecommerce.order.controller;

import com.ecommerce.order.model.Order;
import com.ecommerce.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Tag(name = "Order API", description = "주문 생성 및 조회 API")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        Order response = orderService.createOrder(order);
        log.info("주문 생성: {}", response.getId());
        return ResponseEntity.ok("Order Created: " + response.getId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 조회", description = "주문 ID를 이용하여 주문 정보를 조회합니다.")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order response = orderService.getOrderById(id);
        log.info("주문 조회: {}", response.getId());
        return ResponseEntity.ok(response);
    }
}
