package com.ecommerce.user.logging.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseEventDto extends BaseEventDto {
    Long productId;
    String categoryId;
    double amount;

    public PurchaseEventDto(Long productId, String categoryId, double amount) {
        super(productId, "purchase");
        this.productId = productId;
        this.categoryId = categoryId;
        this.amount = amount;
    }
}
