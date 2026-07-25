package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.MenuItemRequest;
import com.fooddelivery.restaurant.entity.MenuItem;
import com.fooddelivery.restaurant.entity.Restaurant;
import com.fooddelivery.restaurant.exception.ApiException;
import com.fooddelivery.restaurant.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantService restaurantService;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantService restaurantService) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantService = restaurantService;
    }

    public List<MenuItem> getByRestaurant(Long restaurantId) {
        restaurantService.getById(restaurantId); // ensures restaurant exists, 404s otherwise
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new ApiException("Menu item not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public MenuItem create(Long restaurantId, MenuItemRequest request) {
        Restaurant restaurant = restaurantService.getById(restaurantId);
        MenuItem item = new MenuItem(request.getName(), request.getPrice(), request.isAvailable(), restaurant);
        return menuItemRepository.save(item);
    }

    public MenuItem update(Long id, MenuItemRequest request) {
        MenuItem item = getById(id);
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setAvailable(request.isAvailable());
        return menuItemRepository.save(item);
    }

    public void delete(Long id) {
        MenuItem item = getById(id);
        menuItemRepository.delete(item);
    }
}
