package com.ecommerce.order.logging.service;

import com.ecommerce.order.logging.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final Logger clickLogger = LoggerFactory.getLogger("USER_CLICK");
    private final Logger exposeLogger = LoggerFactory.getLogger("USER_EXPOSE");
    private final Logger purchaseLogger = LoggerFactory.getLogger("USER_PURCHASE");
    private final Logger accessLogger = LoggerFactory.getLogger("ACCESS_LOG");
    private final Logger consoleLog = LoggerFactory.getLogger(LogService.class);


    private static final ObjectMapper objectMapper = new ObjectMapper();

    private void logEvent(Logger logger, BaseEventDto event) {
        try {
            consoleLog.info("== log Event ==");
            logger.info("*** logger = {} ***",objectMapper.writeValueAsString(event));
        } catch (Exception e) {
            logger.error(e.getMessage());
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
