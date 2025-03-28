package com.ecommerce.order.logging.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PurchaseEventDto extends BaseEventDto{
    String productId;
    String orderId;
    double amount;

    public PurchaseEventDto(String userId, String productId, String orderId, double amount) {
        super(userId, "purchase");
        this.productId = productId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
