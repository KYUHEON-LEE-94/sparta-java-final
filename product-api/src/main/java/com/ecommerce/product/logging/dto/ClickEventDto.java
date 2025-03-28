package com.ecommerce.product.logging.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClickEventDto extends BaseEventDto {
    private String categoryId;
    private String location;

    public ClickEventDto(Long productId, String categoryId, String location) {
        super(productId, "click");
        this.categoryId = categoryId;
        this.location = location;
    }
}
