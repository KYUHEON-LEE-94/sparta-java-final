package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderCreatedEvent;
import com.ecommerce.order.logging.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaProducer {

  private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
  private static final String TOPIC = "order-created";

  public void sendOrderCreatedEvent(OrderCreatedEvent event) {
    ListenableFuture<SendResult<String, OrderCreatedEvent>> future = kafkaTemplate.send(TOPIC, event);

    future.addCallback(new ListenableFutureCallback<>() {
      @Override
      public void onSuccess(SendResult<String, OrderCreatedEvent> result) {
        log.info("✅ Kafka 메시지 전송 성공 - Topic: {}, Partition: {}, Offset: {}",
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset());
      }


      @Override
      public void onFailure(Throwable ex) {
        log.error("❌ Kafka 메시지 전송 실패 - Topic: {}, Error: {}", TOPIC, ex.getMessage(), ex);
      }
    });
  }
}
