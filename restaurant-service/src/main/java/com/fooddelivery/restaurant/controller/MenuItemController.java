package com.fooddelivery.restaurant.controller;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.entity.MenuItem;
import com.fooddelivery.restaurant.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemService.getByRestaurant(restaurantId));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<MenuItem> getById(@PathVariable Long restaurantId, @PathVariable Long itemId) {
        return ResponseEntity.ok(menuItemService.getById(itemId));
    }

    @PostMapping
    public ResponseEntity<MenuItem> create(@PathVariable Long restaurantId, @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.create(restaurantId, request));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<MenuItem> update(@PathVariable Long restaurantId, @PathVariable Long itemId, @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(menuItemService.update(itemId, request));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long restaurantId, @PathVariable Long itemId) {
        menuItemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}
