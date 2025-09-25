package com.restaurant.repository;

import com.restaurant.entity.OrderItem;
import com.restaurant.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);   // 👈 no lo usas (porque trabajas con Order completo)
    List<OrderItem> findByMenuItemId(Long menuItemId);
}