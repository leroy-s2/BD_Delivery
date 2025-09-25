package com.restaurant.repository;

import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.User;
import com.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByRestaurant(Restaurant restaurant);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}