package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
  @NotBlank
  Long orderId;
  @NotBlank
  String userId;
  @NotBlank
  Long productId;
  @NotBlank
  String shoppingAddress;
  Integer quantity;
  BigDecimal totalPrice;
  OrderStatus status;
  LocalDateTime createdAt;
}
