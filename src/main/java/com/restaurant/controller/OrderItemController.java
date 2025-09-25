package com.restaurant.controller;

import com.restaurant.dto.OrderItemDTO;
import com.restaurant.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order-items")
@CrossOrigin(origins = "*")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemDTO>> getItemsByOrder(@PathVariable Long orderId) {
        try {
            List<OrderItemDTO> items = orderItemService.findByOrderId(orderId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getOrderItemById(@PathVariable Long id) {
        try {
            OrderItemDTO item = orderItemService.findById(id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createOrderItem(@RequestBody OrderItemDTO dto) {
        try {
            OrderItemDTO saved = orderItemService.create(dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDTO> updateOrderItem(@PathVariable Long id, @RequestBody OrderItemDTO dto) {
        try {
            OrderItemDTO updated = orderItemService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        try {
            orderItemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/menu/{menuItemId}")
    public ResponseEntity<List<OrderItemDTO>> getItemsByMenuItem(@PathVariable Long menuItemId) {
        try {
            List<OrderItemDTO> items = orderItemService.findByMenuItemId(menuItemId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


}