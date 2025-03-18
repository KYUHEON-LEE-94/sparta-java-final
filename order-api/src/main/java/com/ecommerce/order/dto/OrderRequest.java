package com.ecommerce.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    String userId;
    String productId;
    String shoppingAddress;
    Integer quantity;
    BigDecimal totalPrice;
    OrderStatus status;
}
