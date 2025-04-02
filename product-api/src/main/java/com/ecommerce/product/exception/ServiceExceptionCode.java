package com.ecommerce.product.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ServiceExceptionCode {
    NOT_FOUND_PRODUCT("NOT_FOUND_PRODUCT", "상품을 찾을 수 없습니다."),
    OUT_OF_STOCK("OUT_OF_STOCK", "재고가 부족합니다."),

    ;
    private final String code;
    private final String message;

    ServiceExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
