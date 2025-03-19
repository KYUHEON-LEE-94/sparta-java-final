package com.ecommerce.product.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
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