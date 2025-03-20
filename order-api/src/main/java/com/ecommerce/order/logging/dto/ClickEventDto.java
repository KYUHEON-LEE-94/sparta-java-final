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
public class ClickEventDto extends BaseEventDto {
    private String productId;
    private String location;

    public ClickEventDto(String userId, String productId, String location) {
        super(userId, "click");
        this.productId = productId;
        this.location = location;
    }
}
