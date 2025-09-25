package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.OrderItemDTO;
import com.restaurant.entity.OrderItem;
import com.restaurant.entity.Order;
import com.restaurant.mappers.OrderItemMapper;
import com.restaurant.repository.OrderItemRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.service.OrderItemService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDTO create(OrderItemDTO dto) throws ServiceException {
        try {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            OrderItem orderItem = orderItemMapper.toEntity(dto);
            orderItem.setOrder(order);

            OrderItem saved = orderItemRepository.save(orderItem);
            return orderItemMapper.toDTO(saved);
        } catch (Exception e) {
            throw new ServiceException("Error al crear item de orden", e);
        }
    }

    @Override
    public OrderItemDTO update(Long id, OrderItemDTO dto) throws ServiceException {
        try {
            OrderItem item = orderItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item de orden no encontrado"));

            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());
            item.setSpecialInstructions(dto.getSpecialInstructions());

            OrderItem updated = orderItemRepository.save(item);
            return orderItemMapper.toDTO(updated);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar item de orden", e);
        }
    }

    @Override
    public OrderItemDTO findById(Long id) throws ServiceException {
        try {
            OrderItem item = orderItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item de orden no encontrado"));
            return orderItemMapper.toDTO(item);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar item de orden", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!orderItemRepository.existsById(id)) {
                throw new ResourceNotFoundException("Item de orden no encontrado");
            }
            orderItemRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar item de orden", e);
        }
    }

    @Override
    public List<OrderItemDTO> findAll() throws ServiceException {
        try {
            List<OrderItem> items = orderItemRepository.findAll();
            return orderItemMapper.toDTOs(items);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener todos los items de orden", e);
        }
    }

    @Override
    public List<OrderItemDTO> findByOrderId(Long orderId) throws ServiceException {
        try {
            List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
            return orderItemMapper.toDTOs(items);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener items de la orden", e);
        }
    }

    @Override
    public List<OrderItemDTO> findByMenuItemId(Long menuItemId) throws ServiceException {
        try {
            List<OrderItem> items = orderItemRepository.findByMenuItemId(menuItemId);
            return orderItemMapper.toDTOs(items);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener items por menuItemId", e);
        }
    }
}
