package com.fooddelivery.order.service;

import com.fooddelivery.order.client.RestaurantServiceClient;
import com.fooddelivery.order.dto.CreateOrderRequest;
import com.fooddelivery.order.dto.MenuItemDto;
import com.fooddelivery.order.dto.OrderItemRequest;
import com.fooddelivery.order.entity.Order;
import com.fooddelivery.order.entity.OrderItem;
import com.fooddelivery.order.entity.OrderStatus;
import com.fooddelivery.order.exception.ApiException;
import com.fooddelivery.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantServiceClient restaurantServiceClient;

    public OrderService(OrderRepository orderRepository, RestaurantServiceClient restaurantServiceClient) {
        this.orderRepository = orderRepository;
        this.restaurantServiceClient = restaurantServiceClient;
    }

    // The core cross-service flow: for every line item, ask restaurant-service
    // for the authoritative price and availability rather than trusting the
    // client. This is the classic reason microservices call each other.
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            MenuItemDto menuItem;
            try {
                menuItem = restaurantServiceClient.getMenuItem(request.getRestaurantId(), itemRequest.getMenuItemId());
            } catch (Exception ex) {
                throw new ApiException("Menu item " + itemRequest.getMenuItemId() + " could not be found", HttpStatus.BAD_REQUEST);
            }

            if (!menuItem.isAvailable()) {
                throw new ApiException("Menu item '" + menuItem.getName() + "' is currently unavailable", HttpStatus.BAD_REQUEST);
            }

            BigDecimal lineTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(lineTotal);

            OrderItem orderItem = new OrderItem(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getPrice(),
                    itemRequest.getQuantity(),
                    order
            );
            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);
        return orderRepository.save(order);
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApiException("Order not found with id: " + id, HttpStatus.NOT_FOUND));
    }

    public List<Order> getByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateStatus(Long id, OrderStatus status) {
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
