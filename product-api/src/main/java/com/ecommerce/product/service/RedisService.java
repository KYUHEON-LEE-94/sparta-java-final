package com.ecommerce.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> void setObject(String key, T object, Integer ttlInSeconds) {
        try {
            String json = objectMapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, json, Duration.ofSeconds(ttlInSeconds));
        } catch (JsonProcessingException e) {
            log.error("[RedisService] setObject error: {}", e.getMessage());
        }
    }

    public <T> T findObject(String key, TypeReference<T> type) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            return StringUtils.hasText(json) ? objectMapper.readValue(json, type) : null;
        } catch (JsonProcessingException e) {
            log.error("[RedisService] findObject error: {}", e.getMessage());
            return null;
        }
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public List<String> getTopProducts(String leaderboardType, int topN) {
        String key = RedisKeyUtil.getLeaderboardKey(leaderboardType);
        Set<String> result = redisTemplate.opsForZSet().reverseRange(key, 0, topN - 1);
        return result != null ? new ArrayList<>(result) : List.of();
    }
}
