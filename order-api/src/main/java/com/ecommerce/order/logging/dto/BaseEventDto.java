package com.ecommerce.order.logging.dto;

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
    private String userId;
    private String eventType;
    private String timestamp;

    public BaseEventDto(String userId, String eventType) {
        this.userId = userId;
        this.eventType = eventType;
        this.timestamp = Instant.now().toString();
    }
}
