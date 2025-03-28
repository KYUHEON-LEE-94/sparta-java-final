package com.ecommerce.user.logging.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseEventDto {
    private Long productId;
    private String eventType;
    private String timestamp;

    public BaseEventDto(Long productId, String eventType) {
        this.productId = productId;
        this.eventType = eventType;
        this.timestamp = Instant.now().toString();
    }
}
