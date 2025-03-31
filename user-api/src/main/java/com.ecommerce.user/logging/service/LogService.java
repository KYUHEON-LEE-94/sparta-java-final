package com.ecommerce.user.logging.service;

import com.ecommerce.user.logging.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void logClickEvent(Long productId, String categoryId, String location) {
        ClickEventDto event = new ClickEventDto(productId, categoryId, location);
        logEvent(clickLogger, event);
    }

    public void logExposeEvent(Long productId, String categoryId, long duration) {
        ExposeEventDto event = new ExposeEventDto(productId, categoryId, duration);
        logEvent(exposeLogger, event);
    }

    public void logPurchaseEvent(Long productId, String categoryId, double amount) {
        PurchaseEventDto event = new PurchaseEventDto(productId, categoryId, amount);
        logEvent(purchaseLogger, event);
    }

    public void logAccessEvent(Long userId, String method, String url, int statusCode, long responseTime) {
        AccessEventDto event = new AccessEventDto(userId, method, url, statusCode, responseTime);
        logEvent(accessLogger, event);
    }
}
