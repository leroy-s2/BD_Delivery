package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.RestaurantDTO;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.User;
import com.restaurant.mappers.RestaurantMapper;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.RestaurantService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantMapper restaurantMapper;

    /**
     * Crear restaurante y asociarlo al usuario creador
     */
    @Override
    public RestaurantDTO create(RestaurantDTO dto, Long userId) throws ServiceException {
        try {
            Restaurant restaurant = restaurantMapper.toEntity(dto);

            // inicializar listas para evitar null
            if (restaurant.getUsers() == null) {
                restaurant.setUsers(new ArrayList<>());
            }

            // Buscar usuario
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            if (user.getRestaurants() == null) {
                user.setRestaurants(new ArrayList<>());
            }

            // Asociar ambos lados
            restaurant.getUsers().add(user);
            user.getRestaurants().add(restaurant);

            // Guardar restaurante
            Restaurant savedRestaurant = restaurantRepository.save(restaurant);

            return restaurantMapper.toDTO(savedRestaurant);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("Error al crear restaurante", e);
        }
    }


    @Override
    public RestaurantDTO update(Long id, RestaurantDTO dto) throws ServiceException {
        try {
            if (!restaurantRepository.existsById(id)) {
                throw new ResourceNotFoundException("Restaurante no encontrado");
            }
            Restaurant restaurant = restaurantMapper.toEntity(dto);
            restaurant.setId(id);
            Restaurant updatedRestaurant = restaurantRepository.save(restaurant);
            return restaurantMapper.toDTO(updatedRestaurant);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar restaurante", e);
        }
    }

    @Override
    public RestaurantDTO findById(Long id) throws ServiceException {
        try {
            Restaurant restaurant = restaurantRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));
            return restaurantMapper.toDTO(restaurant);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar restaurante", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!restaurantRepository.existsById(id)) {
                throw new ResourceNotFoundException("Restaurante no encontrado");
            }
            restaurantRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar restaurante", e);
        }
    }

    @Override
    public List<RestaurantDTO> findAll() throws ServiceException {
        try {
            List<Restaurant> restaurants = restaurantRepository.findAll();
            return restaurantMapper.toDTOs(restaurants);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener restaurantes", e);
        }
    }

    @Override
    public List<RestaurantDTO> searchByName(String name) throws ServiceException {
        try {
            List<Restaurant> restaurants = restaurantRepository.findByNameContainingIgnoreCase(name);
            return restaurantMapper.toDTOs(restaurants);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar restaurantes por nombre", e);
        }
    }

    @Override
    public List<RestaurantDTO> searchByAddress(String address) throws ServiceException {
        try {
            List<Restaurant> restaurants = restaurantRepository.findByAddressContainingIgnoreCase(address);
            return restaurantMapper.toDTOs(restaurants);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar restaurantes por dirección", e);
        }
    }

    /**
     * Restaurantes asociados a un usuario
     */
    @Override
    public List<RestaurantDTO> findByUserId(Long userId) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            return restaurantMapper.toDTOs(user.getRestaurants());
        } catch (Exception e) {
            throw new ServiceException("Error al obtener restaurantes del usuario", e);
        }
    }
}
