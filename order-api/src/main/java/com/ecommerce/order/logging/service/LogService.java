package com.ecommerce.order.logging.service;

import com.ecommerce.order.logging.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {
    private static final Logger clickLogger = LoggerFactory.getLogger("USER_CLICK");
    private static final Logger exposeLogger = LoggerFactory.getLogger("USER_EXPOSE");
    private static final Logger purchaseLogger = LoggerFactory.getLogger("USER_PURCHASE");
    private static final Logger accessLogger = LoggerFactory.getLogger("ACCESS_LOG");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private void logEvent(Logger logger, BaseEventDto event) {
        try {
            logger.info(objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logClickEvent(String userId, String productId, String location) {
        ClickEventDto event = new ClickEventDto(userId, productId, location);
        logEvent(clickLogger, event);
    }

    public void logExposeEvent(String userId, String productId, long duration) {
        ExposeEventDto event = new ExposeEventDto(userId, productId, duration);
        logEvent(exposeLogger, event);
    }

    public void logPurchaseEvent(String userId, String productId, String orderId, double amount) {
        PurchaseEventDto event = new PurchaseEventDto(userId, productId, orderId, amount);
        logEvent(purchaseLogger, event);
    }

    public void logAccessEvent(String userId, String method, String url, int statusCode, long responseTime) {
        AccessEventDto event = new AccessEventDto(userId, method, url, statusCode, responseTime);
        logEvent(accessLogger, event);
    }
}
