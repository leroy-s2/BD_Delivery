package com.restaurant.service;

import com.restaurant.dto.UserDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;

public interface UserService extends GenericService<com.restaurant.entity.User, UserDTO, Long> {
    UserDTO findByEmail(String email) throws ServiceException;
    boolean existsByEmail(String email) throws ServiceException;
    UserDTO authenticate(String email, String password) throws ServiceException;
}