package com.restaurant.service;

import com.restaurant.dto.RestaurantDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;
import java.util.List;

public interface RestaurantService extends GenericService<com.restaurant.entity.Restaurant, RestaurantDTO, Long> {
    List<RestaurantDTO> searchByName(String name) throws ServiceException;
    List<RestaurantDTO> searchByAddress(String address) throws ServiceException;
}