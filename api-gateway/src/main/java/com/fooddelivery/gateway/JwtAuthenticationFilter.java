package com.fooddelivery.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpMethod;

import javax.crypto.SecretKey;
import java.util.List;

/**
 * Validates JWTs at the edge for protected routes so downstream services
 * don't each need to re-implement token parsing. Public auth endpoints
 * (register/login) are allowed through untouched.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    // Must match the secret used by user-service to sign tokens.
    // In a real deployment, pull this from a config server / vault instead of hardcoding.
    private static final String SECRET = "food-delivery-super-secret-key-change-me-in-prod-0123456789";

    private static final List<String> OPEN_ENDPOINTS = List.of(
            "/api/users/auth/register",
            "/api/users/auth/login"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Allow CORS preflight requests without JWT
        if (request.getMethod() == HttpMethod.OPTIONS) {
        return chain.filter(exchange);
        }

        if (OPEN_ENDPOINTS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders == null || authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeaders.get(0).substring(7);
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (Exception ex) {
            return unauthorized(exchange);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
