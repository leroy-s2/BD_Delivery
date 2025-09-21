package com.restaurant.mappers;

import com.restaurant.dto.ReservationDTO;
import com.restaurant.entity.Reservation;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper implements BaseMappers<Reservation, ReservationDTO> {

    @Override
    public ReservationDTO toDTO(Reservation entity) {
        if (entity == null) return null;

        ReservationDTO dto = new ReservationDTO();
        dto.setId(entity.getId());
        dto.setReservationDateTime(entity.getReservationDateTime());
        dto.setPartySize(entity.getPartySize());
        dto.setSpecialRequests(entity.getSpecialRequests());
        dto.setStatus(entity.getStatus().name());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getFullName());
        }

        if (entity.getRestaurant() != null) {
            dto.setRestaurantId(entity.getRestaurant().getId());
            dto.setRestaurantName(entity.getRestaurant().getName());
        }

        return dto;
    }

    @Override
    public Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;

        Reservation entity = new Reservation();
        entity.setId(dto.getId());
        entity.setReservationDateTime(dto.getReservationDateTime());
        entity.setPartySize(dto.getPartySize());
        entity.setSpecialRequests(dto.getSpecialRequests());
        entity.setCreatedAt(dto.getCreatedAt());

        // User, Restaurant and Status should be set separately
        return entity;
    }

    @Override
    public List<ReservationDTO> toDTOs(List<Reservation> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Reservation> toEntityList(List<ReservationDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}