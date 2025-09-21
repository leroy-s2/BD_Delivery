package com.restaurant.service;

import com.restaurant.dto.OrderDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;
import java.util.List;

public interface OrderService extends GenericService<com.restaurant.entity.Order, OrderDTO, Long> {
    List<OrderDTO> findByUserId(Long userId) throws ServiceException;
    List<OrderDTO> findByRestaurantId(Long restaurantId) throws ServiceException;
    OrderDTO updateStatus(Long orderId, String status) throws ServiceException;
    OrderDTO createOrderWithItems(OrderDTO orderDTO) throws ServiceException;
}