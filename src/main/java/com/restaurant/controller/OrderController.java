package com.restaurant.controller;

import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderItemDTO;
import com.restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@PathVariable Long userId) {
        try {
            List<OrderDTO> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        try {
            OrderDTO order = orderService.findById(id);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData) {
        try {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setUserId(Long.valueOf(orderData.get("userId").toString()));
            orderDTO.setRestaurantId(Long.valueOf(orderData.get("restaurantId").toString()));
            orderDTO.setTotalAmount(new BigDecimal(orderData.get("totalAmount").toString()));
            orderDTO.setDeliveryAddress(orderData.get("deliveryAddress").toString());
            orderDTO.setOrderType(orderData.get("orderType").toString());

            // Process order items
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) orderData.get("items");
            List<OrderItemDTO> orderItems = new ArrayList<>();

            for (Map<String, Object> itemData : items) {
                OrderItemDTO orderItemDTO = new OrderItemDTO();
                orderItemDTO.setMenuItemId(Long.valueOf(itemData.get("menuItemId").toString()));
                orderItemDTO.setQuantity(Integer.valueOf(itemData.get("quantity").toString()));
                orderItemDTO.setPrice(new BigDecimal(itemData.get("price").toString()));

                String specialInstructions = itemData.get("specialInstructions") != null ?
                        itemData.get("specialInstructions").toString() : "";
                orderItemDTO.setSpecialInstructions(specialInstructions);

                orderItems.add(orderItemDTO);
            }

            orderDTO.setOrderItems(orderItems);

            OrderDTO savedOrder = orderService.createOrderWithItems(orderDTO);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear el pedido: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO updatedOrder = orderService.update(id, orderDTO);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}