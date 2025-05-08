package com.ecommerce.product.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
  @NotBlank
  String name;

  @NotBlank
  String description;

  @NotNull
  BigDecimal price;

  @NotNull
  Integer stock;

  String categoryId;

  DiscountType discountType;
  BigDecimal discountAmount;
  BigDecimal discountRate;
}