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
public class AccessEventDto extends BaseEventDto{
    String method;
    String url;
    int statusCode;
    long responseTime;

    public AccessEventDto(String userId, String method, String url, int statusCode, long responseTime) {
        super(userId, "access");
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }
}
