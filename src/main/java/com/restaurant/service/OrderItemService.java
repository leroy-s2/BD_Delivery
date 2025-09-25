package com.restaurant.service;

import com.restaurant.dto.OrderItemDTO;
import com.restaurant.entity.Order;
import com.restaurant.entity.OrderItem;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;
import java.util.List;

public interface OrderItemService extends GenericService<OrderItem, OrderItemDTO, Long> {
    List<OrderItemDTO> findByOrderId(Long orderId) throws ServiceException;
    List<OrderItemDTO> findByMenuItemId(Long menuItemId) throws ServiceException;
}