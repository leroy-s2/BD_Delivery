package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.OrderItemDTO;
import com.restaurant.entity.*;
import com.restaurant.mappers.OrderMapper;
import com.restaurant.repository.*;
import com.restaurant.service.OrderService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderDTO create(OrderDTO dto) throws ServiceException {
        try {
            Order order = orderMapper.toEntity(dto);

            // Set user and restaurant
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            order.setUser(user);
            order.setRestaurant(restaurant);
            order.setStatus(OrderStatus.PENDING);
            order.setOrderType(OrderType.valueOf(dto.getOrderType()));
            order.setCreatedAt(LocalDateTime.now());

            Order savedOrder = orderRepository.save(order);
            return orderMapper.toDTO(savedOrder);
        } catch (Exception e) {
            throw new ServiceException("Error al crear orden", e);
        }
    }

    @Override
    public OrderDTO update(Long id, OrderDTO dto) throws ServiceException {
        try {
            Order existingOrder = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            existingOrder.setTotalAmount(dto.getTotalAmount());
            existingOrder.setDeliveryAddress(dto.getDeliveryAddress());
            existingOrder.setDeliveryTime(dto.getDeliveryTime());

            Order updatedOrder = orderRepository.save(existingOrder);
            return orderMapper.toDTO(updatedOrder);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar orden", e);
        }
    }

    @Override
    public OrderDTO findById(Long id) throws ServiceException {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));
            return orderMapper.toDTO(order);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar orden", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!orderRepository.existsById(id)) {
                throw new ResourceNotFoundException("Orden no encontrada");
            }
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar orden", e);
        }
    }

    @Override
    public List<OrderDTO> findAll() throws ServiceException {
        try {
            List<Order> orders = orderRepository.findAll();
            return orderMapper.toDTOs(orders);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener órdenes", e);
        }
    }

    @Override
    public List<OrderDTO> findByUserId(Long userId) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
            return orderMapper.toDTOs(orders);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener órdenes del usuario", e);
        }
    }

    @Override
    public List<OrderDTO> findByRestaurantId(Long restaurantId) throws ServiceException {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            List<Order> orders = orderRepository.findByRestaurant(restaurant);
            return orderMapper.toDTOs(orders);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener órdenes del restaurante", e);
        }
    }

    @Override
    public OrderDTO updateStatus(Long orderId, String status) throws ServiceException {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            order.setStatus(OrderStatus.valueOf(status));
            Order updatedOrder = orderRepository.save(order);
            return orderMapper.toDTO(updatedOrder);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar estado de orden", e);
        }
    }

    @Override
    public OrderDTO createOrderWithItems(OrderDTO orderDTO) throws ServiceException {
        try {
            // Create the order first
            Order order = new Order();

            // Set user and restaurant
            User user = userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Restaurant restaurant = restaurantRepository.findById(orderDTO.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            order.setUser(user);
            order.setRestaurant(restaurant);
            order.setTotalAmount(orderDTO.getTotalAmount());
            order.setDeliveryAddress(orderDTO.getDeliveryAddress());
            order.setStatus(OrderStatus.PENDING);
            order.setOrderType(OrderType.valueOf(orderDTO.getOrderType()));
            order.setCreatedAt(LocalDateTime.now());

            Order savedOrder = orderRepository.save(order);

            // Create order items if provided
            if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
                for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                    MenuItem menuItem = menuItemRepository.findById(itemDTO.getMenuItemId())
                            .orElseThrow(() -> new ResourceNotFoundException("Item del menú no encontrado"));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(itemDTO.getQuantity());
                    orderItem.setPrice(itemDTO.getPrice());
                    orderItem.setSpecialInstructions(itemDTO.getSpecialInstructions());

                    orderItemRepository.save(orderItem);
                }
            }

            // Reload the order with items to return complete DTO
            Order completeOrder = orderRepository.findById(savedOrder.getId()).orElse(savedOrder);
            return orderMapper.toDTO(completeOrder);

        } catch (Exception e) {
            throw new ServiceException("Error al crear orden con items", e);
        }
    }
}