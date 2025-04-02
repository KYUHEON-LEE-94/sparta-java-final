package com.ecommerce.order.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum ServiceExceptionCode {
    NOT_FOUND_ORDER("NOT_FOUND_ORDER", "주문을 찾을 수 없습니다."),
    ;
    private final String code;
    private final String message;

    ServiceExceptionCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
