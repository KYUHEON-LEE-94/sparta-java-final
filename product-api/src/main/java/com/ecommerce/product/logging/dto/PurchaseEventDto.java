package com.ecommerce.product.logging.dto;

import com.ecommerce.product.logging.dto.BaseEventDto;
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
