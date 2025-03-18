package com.ecommerce.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponseDto {
  Long id;
  String name;
  String description;
  BigDecimal price;
  Integer stock;
  String categoryId;
  Boolean deletedYn;
  DiscountType discountType;
  BigDecimal discountAmount;
  BigDecimal discountRate;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
}
