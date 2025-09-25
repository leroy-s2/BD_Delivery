package com.restaurant.controller;

import com.restaurant.dto.RestaurantDTO;
import com.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        try {
            List<RestaurantDTO> restaurants = restaurantService.findAll();
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        try {
            RestaurantDTO restaurant = restaurantService.findById(id);
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByUser(@PathVariable Long userId) {
        try {
            List<RestaurantDTO> restaurants = restaurantService.findByUserId(userId);
            return ResponseEntity.ok(restaurants);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        try {
            if (restaurantDTO.getUserId() == null) {
                return ResponseEntity.badRequest().build();
            }
            RestaurantDTO savedRestaurant = restaurantService.create(restaurantDTO, restaurantDTO.getUserId());
            return ResponseEntity.ok(savedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDTO restaurantDTO) {
        try {
            RestaurantDTO updatedRestaurant = restaurantService.update(id, restaurantDTO);
            return ResponseEntity.ok(updatedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<RestaurantDTO> createRestaurant(
            @PathVariable Long userId,
            @RequestBody RestaurantDTO restaurantDTO) {
        try {
            RestaurantDTO savedRestaurant = restaurantService.create(restaurantDTO, userId);
            return ResponseEntity.ok(savedRestaurant);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
