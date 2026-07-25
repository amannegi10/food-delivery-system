package com.fooddelivery.order.dto;

import java.math.BigDecimal;

// Mirrors the shape of restaurant-service's MenuItem response.
// Kept as a lightweight DTO here rather than sharing a JAR between
// services, which is a deliberate microservices tradeoff: each service
// owns its own model of the data it needs.
public class MenuItemDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private boolean available;
    private Long restaurantId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}
