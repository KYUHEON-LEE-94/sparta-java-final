package com.ecommerce.product.service;

public class RedisKeyUtil {
    private static final String PRODUCT_PREFIX = "products";
    private static final String LEADERBOARD_PREFIX = "leaderboard";

    public static String getProductKey(String condition) {
        return String.format("%s:%s", PRODUCT_PREFIX, condition);
    }

    public static String getLeaderboardKey(String leaderboardType) {
        return String.format("%s:%s", LEADERBOARD_PREFIX, leaderboardType);
    }
}
