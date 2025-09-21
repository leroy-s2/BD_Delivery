package com.restaurant.service;

import com.restaurant.dto.MenuItemDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;
import java.util.List;

public interface MenuItemService extends GenericService<com.restaurant.entity.MenuItem, MenuItemDTO, Long> {
    List<MenuItemDTO> findByRestaurantId(Long restaurantId) throws ServiceException;
    List<MenuItemDTO> findByCategory(String category) throws ServiceException;
    List<MenuItemDTO> findAvailableByRestaurantId(Long restaurantId) throws ServiceException;
}