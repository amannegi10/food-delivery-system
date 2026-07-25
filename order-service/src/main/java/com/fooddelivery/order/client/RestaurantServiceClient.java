package com.fooddelivery.order.client;

import com.fooddelivery.order.dto.MenuItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// "restaurant-service" here is the Eureka application name (spring.application.name
// in restaurant-service's application.yml), not a URL — Feign + Eureka resolve it
// to an actual host:port via client-side load balancing.
@FeignClient(name = "restaurant-service")
public interface RestaurantServiceClient {

    @GetMapping("/api/restaurants/{restaurantId}/menu-items/{itemId}")
    MenuItemDto getMenuItem(@PathVariable("restaurantId") Long restaurantId, @PathVariable("itemId") Long itemId);
}
