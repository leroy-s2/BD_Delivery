package com.restaurant.mappers;

import com.restaurant.dto.OrderItemDTO;
import com.restaurant.entity.OrderItem;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper implements BaseMappers<OrderItem, OrderItemDTO> {

    @Override
    public OrderItemDTO toDTO(OrderItem entity) {
        if (entity == null) return null;

        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setSpecialInstructions(entity.getSpecialInstructions());

        // Relación con Order
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }

        // Relación con MenuItem
        if (entity.getMenuItem() != null) {
            dto.setMenuItemId(entity.getMenuItem().getId());
            dto.setMenuItemName(entity.getMenuItem().getName()); // 👈 clave para Android
        }

        return dto;
    }

    @Override
    public OrderItem toEntity(OrderItemDTO dto) {
        if (dto == null) return null;

        OrderItem entity = new OrderItem();
        entity.setId(dto.getId());
        entity.setQuantity(dto.getQuantity());
        entity.setPrice(dto.getPrice());
        entity.setSpecialInstructions(dto.getSpecialInstructions());

        // 👇 Nota importante:
        // No seteamos Order ni MenuItem aquí, porque esas entidades
        // deben recuperarse con sus repositorios en el ServiceImpl
        // (por ejemplo, orderRepository.findById(...) )
        return entity;
    }

    @Override
    public List<OrderItemDTO> toDTOs(List<OrderItem> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderItem> toEntityList(List<OrderItemDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
