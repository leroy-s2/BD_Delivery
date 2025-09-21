package com.restaurant.mappers;

import com.restaurant.dto.RestaurantDTO;
import com.restaurant.entity.Restaurant;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RestaurantMapper implements BaseMappers<Restaurant, RestaurantDTO> {

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Override
    public RestaurantDTO toDTO(Restaurant entity) {
        if (entity == null) return null;

        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setImageUrl(entity.getImageUrl());
        dto.setCapacity(entity.getCapacity());
        dto.setOpenTime(entity.getOpenTime());
        dto.setCloseTime(entity.getCloseTime());

        if (entity.getMenuItems() != null) {
            dto.setMenuItems(menuItemMapper.toDTOs(entity.getMenuItems()));
        }

        return dto;
    }

    @Override
    public Restaurant toEntity(RestaurantDTO dto) {
        if (dto == null) return null;

        Restaurant entity = new Restaurant();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setImageUrl(dto.getImageUrl());
        entity.setCapacity(dto.getCapacity());
        entity.setOpenTime(dto.getOpenTime());
        entity.setCloseTime(dto.getCloseTime());

        return entity;
    }

    @Override
    public List<RestaurantDTO> toDTOs(List<Restaurant> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> toEntityList(List<RestaurantDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
