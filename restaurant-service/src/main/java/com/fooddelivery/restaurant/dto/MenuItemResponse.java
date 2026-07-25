package com.fooddelivery.restaurant.dto;

import java.math.BigDecimal;

public class MenuItemResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private boolean available;
    private Long restaurantId;

    public MenuItemResponse(Long id, String name, BigDecimal price, boolean available, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.restaurantId = restaurantId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public Long getRestaurantId() { return restaurantId; }
}
