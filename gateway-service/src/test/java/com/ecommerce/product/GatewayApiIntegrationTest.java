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
        classes = com.ecommerce.gateway.GatewayServiceApplication.class
)
@AutoConfigureWebTestClient
public class GatewayApiIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    String secretKey = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=";

    private final String token = Jwts.builder()
            .subject("test-user")
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(getSigningKey(secretKey))
            .compact();

    @Test
    void shouldBlockRequestWithoutJwt() {
        webTestClient.get().uri("http://localhost:8081/product")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void shouldAllowRequestWithValidJwt() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .build();

        webTestClient.get()
                .uri("/product")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();
    }

    private SecretKey getSigningKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
