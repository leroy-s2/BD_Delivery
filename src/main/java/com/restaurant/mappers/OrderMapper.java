package com.restaurant.mappers;

import com.restaurant.dto.OrderDTO;
import com.restaurant.entity.Order;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper implements BaseMappers<Order, OrderDTO> {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderDTO toDTO(Order entity) {
        if (entity == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(entity.getId());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setDeliveryAddress(entity.getDeliveryAddress());
        dto.setStatus(entity.getStatus().name());
        dto.setOrderType(entity.getOrderType().name());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setDeliveryTime(entity.getDeliveryTime());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getFullName());
        }

        if (entity.getRestaurant() != null) {
            dto.setRestaurantId(entity.getRestaurant().getId());
            dto.setRestaurantName(entity.getRestaurant().getName());
        }

        if (entity.getOrderItems() != null) {
            dto.setOrderItems(orderItemMapper.toDTOs(entity.getOrderItems()));
        }

        return dto;
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order entity = new Order();
        entity.setId(dto.getId());
        entity.setTotalAmount(dto.getTotalAmount());
        entity.setDeliveryAddress(dto.getDeliveryAddress());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setDeliveryTime(dto.getDeliveryTime());

        // User, Restaurant, Status and OrderType should be set separately
        return entity;
    }

    @Override
    public List<OrderDTO> toDTOs(List<Order> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Order> toEntityList(List<OrderDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
