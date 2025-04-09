package com.ecommerce.product.service;

public class RedisKeyUtil {
    private static final String PRODUCT_PREFIX = "products";
    private static final String PURCHASE_PREFIX = "purchases";
    private static final String LEADERBOARD_PREFIX = "leaderboard";

    public static String getProductKey(String condition) {
        return String.format("%s:%s", PRODUCT_PREFIX, condition);
    }

    public static String getPurchaseKey() {
        return String.format("%s:%s", PURCHASE_PREFIX, "products");
    }

    public static String getLeaderboardKey(String leaderboardType) {
        return String.format("%s:%s", LEADERBOARD_PREFIX, leaderboardType);
    }
}
