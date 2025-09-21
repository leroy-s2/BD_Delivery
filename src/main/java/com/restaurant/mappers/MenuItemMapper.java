package com.restaurant.mappers;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuItemMapper implements BaseMappers<MenuItem, MenuItemDTO> {

    @Override
    public MenuItemDTO toDTO(MenuItem entity) {
        if (entity == null) return null;

        MenuItemDTO dto = new MenuItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCategory(entity.getCategory());
        dto.setAvailable(entity.getAvailable());

        if (entity.getRestaurant() != null) {
            dto.setRestaurantId(entity.getRestaurant().getId());
            dto.setRestaurantName(entity.getRestaurant().getName());
        }

        return dto;
    }

    @Override
    public MenuItem toEntity(MenuItemDTO dto) {
        if (dto == null) return null;

        MenuItem entity = new MenuItem();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImageUrl(dto.getImageUrl());
        entity.setCategory(dto.getCategory());
        entity.setAvailable(dto.getAvailable());

        // Restaurant should be set separately
        return entity;
    }

    @Override
    public List<MenuItemDTO> toDTOs(List<MenuItem> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> toEntityList(List<MenuItemDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}