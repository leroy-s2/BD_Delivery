package com.restaurant.service;

import com.restaurant.dto.RestaurantDTO;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface RestaurantService {
    RestaurantDTO create(RestaurantDTO dto, Long userId) throws ServiceException;
    RestaurantDTO update(Long id, RestaurantDTO dto) throws ServiceException;
    RestaurantDTO findById(Long id) throws ServiceException;
    void deleteById(Long id) throws ServiceException;
    List<RestaurantDTO> findAll() throws ServiceException;
    List<RestaurantDTO> searchByName(String name) throws ServiceException;
    List<RestaurantDTO> searchByAddress(String address) throws ServiceException;

    // 🔹 Restaurantes por usuario
    List<RestaurantDTO> findByUserId(Long userId) throws ServiceException;
}
