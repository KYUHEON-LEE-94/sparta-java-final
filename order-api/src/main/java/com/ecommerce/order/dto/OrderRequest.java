package com.ecommerce.order.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    @NotBlank
    String userId;
    @NotBlank
    String productId;
    @NotBlank
    String shoppingAddress;
    Integer quantity;
    BigDecimal totalPrice;
    OrderStatus status;
}
