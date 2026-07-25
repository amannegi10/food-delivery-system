package com.fooddelivery.restaurant.service;

import com.fooddelivery.restaurant.dto.RestaurantRequest;
import com.fooddelivery.restaurant.entity.Restaurant;
import com.fooddelivery.restaurant.exception.ApiException;
import com.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new ApiException("Restaurant not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public Restaurant create(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant(request.getName(), request.getAddress(), request.getCuisine());
        return restaurantRepository.save(restaurant);
    }

    public Restaurant update(Long id, RestaurantRequest request) {
        Restaurant restaurant = getById(id);
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setCuisine(request.getCuisine());
        return restaurantRepository.save(restaurant);
    }

    public void delete(Long id) {
        Restaurant restaurant = getById(id);
        restaurantRepository.delete(restaurant);
    }
}
