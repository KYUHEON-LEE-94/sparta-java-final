package com.ecommerce.product;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import javax.crypto.SecretKey;
import java.util.Date;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.ecommerce.gateway.GatewayServiceApplication.class
)
@AutoConfigureWebTestClient
public class GatewayApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private final String token = Jwts.builder()
            .subject("test-user")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(getSigningKey("mySecretKey"))
            .compact();

    @Test
    void shouldBlockRequestWithoutJwt() {
        webTestClient.get().uri("http://localhost:8082/product")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldAllowRequestWithValidJwt() {
        webTestClient.get().uri("http://localhost:8082/product")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk(); // product-service가 잘 연결되어 있어야 200
    }

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
