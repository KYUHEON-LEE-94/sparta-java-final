package com.ecommerce.product.service;

import com.ecommerce.product.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaListener {

  private final ProductService productService;
  private static final String TOPIC = "order-created";

  @KafkaListener(topics = TOPIC, groupId = "product-group")
  public void handleOrderCreated(OrderCreatedEvent event) {
    log.info("📦 수신된 Kafka 메시지 - 주문 ID: {}, 상품 ID: {}, 수량: {}",
        event.getOrderId(), event.getProductId(), event.getQuantity());

    productService.decreaseProduct(event);
  }
}
