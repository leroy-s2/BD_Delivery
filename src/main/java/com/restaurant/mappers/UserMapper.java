package com.restaurant.mappers;

import com.restaurant.dto.UserDTO;
import com.restaurant.entity.User;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper implements BaseMappers<User, UserDTO> {

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setFullName(entity.getFullName());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setRole(entity.getRole().name());
        return dto;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User entity = new User();
        entity.setId(dto.getId());
        entity.setEmail(dto.getEmail());
        entity.setFullName(dto.getFullName());
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress());
        // Note: Role and password should be handled separately for security
        return entity;
    }

    @Override
    public List<UserDTO> toDTOs(List<User> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<User> toEntityList(List<UserDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}