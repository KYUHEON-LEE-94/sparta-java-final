package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.logging.service.LogService;
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
    private final LogService logService;

    @PostMapping
    @Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest order) {
        OrderResponse response = orderService.createOrder(order);
        logService.logClickEvent(response.getUserId(), response.getUserId(), "location");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "주문 조회", description = "주문 ID를 이용하여 주문 정보를 조회합니다.")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {

        long start = System.currentTimeMillis();

        OrderResponse response = orderService.getOrderById(id);

        long end = System.currentTimeMillis();
        long responseTime = end - start;

        logService.logAccessEvent(response.getUserId(),"GET","/order", 200, responseTime);
        return ResponseEntity.ok(response);
    }
}
