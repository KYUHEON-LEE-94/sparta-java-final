package com.ecommerce.product.logging.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessEventDto extends BaseEventDto{
    String method;
    String url;
    int statusCode;
    long responseTime;

    public AccessEventDto(Long userId, String method, String url, int statusCode, long responseTime) {
        super(userId, "access");
        this.method = method;
        this.url = url;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }
}
