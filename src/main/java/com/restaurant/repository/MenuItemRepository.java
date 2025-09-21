package com.restaurant.repository;

import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    List<MenuItem> findByRestaurantAndAvailable(Restaurant restaurant, boolean available);
    List<MenuItem> findByCategory(String category);
}