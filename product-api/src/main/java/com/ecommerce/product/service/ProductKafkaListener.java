package com.ecommerce.product.service;

import com.ecommerce.product.dto.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductKafkaListener {

  private final ProductService productService;
  private static final String TOPIC = "order-created";

  @KafkaListener(
          topics = "order-created",
          groupId = "product-group",
          containerFactory = "kafkaListenerContainerFactory"
  )
  public void handleOrderCreated(OrderCreatedEvent event) {
    try {
      log.info("📦 수신된 Kafka 메시지: {}", event);
      productService.decreaseProduct(event);
    } catch (Exception e) {
      log.error("❌ Kafka 메시지 처리 실패: {}", e.getMessage(), e);
    }
  }
}
