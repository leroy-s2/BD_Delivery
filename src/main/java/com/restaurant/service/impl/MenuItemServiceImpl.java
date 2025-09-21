package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.MenuItemDTO;
import com.restaurant.entity.MenuItem;
import com.restaurant.entity.Restaurant;
import com.restaurant.mappers.MenuItemMapper;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.service.MenuItemService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuItemServiceImpl implements MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemMapper menuItemMapper;

    @Override
    public MenuItemDTO create(MenuItemDTO dto) throws ServiceException {
        try {
            MenuItem menuItem = menuItemMapper.toEntity(dto);

            // Set restaurant if provided
            if (dto.getRestaurantId() != null) {
                Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));
                menuItem.setRestaurant(restaurant);
            }

            MenuItem savedMenuItem = menuItemRepository.save(menuItem);
            return menuItemMapper.toDTO(savedMenuItem);
        } catch (Exception e) {
            throw new ServiceException("Error al crear item del menú", e);
        }
    }

    @Override
    public MenuItemDTO update(Long id, MenuItemDTO dto) throws ServiceException {
        try {
            MenuItem existingMenuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item del menú no encontrado"));

            // Update fields
            existingMenuItem.setName(dto.getName());
            existingMenuItem.setDescription(dto.getDescription());
            existingMenuItem.setPrice(dto.getPrice());
            existingMenuItem.setImageUrl(dto.getImageUrl());
            existingMenuItem.setCategory(dto.getCategory());
            existingMenuItem.setAvailable(dto.getAvailable());

            MenuItem updatedMenuItem = menuItemRepository.save(existingMenuItem);
            return menuItemMapper.toDTO(updatedMenuItem);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar item del menú", e);
        }
    }

    @Override
    public MenuItemDTO findById(Long id) throws ServiceException {
        try {
            MenuItem menuItem = menuItemRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Item del menú no encontrado"));
            return menuItemMapper.toDTO(menuItem);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar item del menú", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!menuItemRepository.existsById(id)) {
                throw new ResourceNotFoundException("Item del menú no encontrado");
            }
            menuItemRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar item del menú", e);
        }
    }

    @Override
    public List<MenuItemDTO> findAll() throws ServiceException {
        try {
            List<MenuItem> menuItems = menuItemRepository.findAll();
            return menuItemMapper.toDTOs(menuItems);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener items del menú", e);
        }
    }

    @Override
    public List<MenuItemDTO> findByRestaurantId(Long restaurantId) throws ServiceException {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            List<MenuItem> menuItems = menuItemRepository.findByRestaurant(restaurant);
            return menuItemMapper.toDTOs(menuItems);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener items del menú por restaurante", e);
        }
    }

    @Override
    public List<MenuItemDTO> findByCategory(String category) throws ServiceException {
        try {
            List<MenuItem> menuItems = menuItemRepository.findByCategory(category);
            return menuItemMapper.toDTOs(menuItems);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar items por categoría", e);
        }
    }

    @Override
    public List<MenuItemDTO> findAvailableByRestaurantId(Long restaurantId) throws ServiceException {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            List<MenuItem> menuItems = menuItemRepository.findByRestaurantAndAvailable(restaurant, true);
            return menuItemMapper.toDTOs(menuItems);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener items disponibles", e);
        }
    }
}