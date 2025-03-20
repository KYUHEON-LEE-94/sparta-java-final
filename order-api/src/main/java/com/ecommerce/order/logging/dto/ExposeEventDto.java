package com.ecommerce.order.logging.dto;

import com.ecommerce.order.dto.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExposeEventDto extends BaseEventDto{
    String productId;
    long exposeDuration;

    public ExposeEventDto(String userId, String productId, long exposeDuration) {
        super(userId, "expose");
        this.productId = productId;
        this.exposeDuration = exposeDuration;
    }
}
