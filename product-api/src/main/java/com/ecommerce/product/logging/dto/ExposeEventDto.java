package com.ecommerce.product.logging.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExposeEventDto extends BaseEventDto{
    String categoryId;
    long exposeDuration;

    public ExposeEventDto(Long productId, String categoryId, long exposeDuration) {
        super(productId, "expose");
        this.categoryId = categoryId;
        this.exposeDuration = exposeDuration;
    }
}
