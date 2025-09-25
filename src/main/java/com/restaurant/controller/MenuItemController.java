package com.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.dto.MenuItemDTO;
import com.restaurant.service.MenuItemService;
import com.restaurant.service.Azure.AzureBlobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menu-items")
@CrossOrigin(origins = "*")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private AzureBlobService azureBlobService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        try {
            List<MenuItemDTO> menuItems = menuItemService.findAvailableByRestaurantId(restaurantId);
            return ResponseEntity.ok(menuItems);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        try {
            MenuItemDTO menuItem = menuItemService.findById(id);
            return ResponseEntity.ok(menuItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO savedMenuItem = menuItemService.create(menuItemDTO);
            return ResponseEntity.ok(savedMenuItem);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🔹 Nuevo endpoint para subir con imagen
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMenuItemWithImage(
            @RequestPart("menuItem") String menuItemJson,
            @RequestPart("image") MultipartFile image) {
        try {
            // Deserializa el JSON manualmente
            ObjectMapper mapper = new ObjectMapper();
            MenuItemDTO menuItemDTO = mapper.readValue(menuItemJson, MenuItemDTO.class);

            String imageUrl = azureBlobService.uploadFile(image);
            menuItemDTO.setImageUrl(imageUrl);
            MenuItemDTO savedMenuItem = menuItemService.create(menuItemDTO);
            return ResponseEntity.ok(savedMenuItem);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO updatedMenuItem = menuItemService.update(id, menuItemDTO);
            return ResponseEntity.ok(updatedMenuItem);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        try {
            menuItemService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
