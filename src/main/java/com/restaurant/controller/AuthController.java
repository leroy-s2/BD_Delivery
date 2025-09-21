package com.restaurant.controller;

import com.restaurant.dto.UserDTO;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        try {
            // Check if user exists
            if (userService.existsByEmail(request.get("email"))) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "El email ya está registrado");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Create user DTO
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(request.get("email"));
            userDTO.setFullName(request.get("fullName"));
            userDTO.setPhone(request.get("phone"));
            userDTO.setAddress(request.get("address"));

            // Create user through service
            UserDTO savedUser = userService.create(userDTO);

            // Separate password handling (should be in service)
            // For now, we'll handle it here but ideally should be in UserService

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("user", Map.of(
                    "id", savedUser.getId(),
                    "email", savedUser.getEmail(),
                    "fullName", savedUser.getFullName(),
                    "role", savedUser.getRole()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            UserDTO user = userService.authenticate(email, password);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login exitoso");
            response.put("token", "fake-jwt-token-" + user.getId());
            response.put("user", Map.of(
                    "id", user.getId(),
                    "email", user.getEmail(),
                    "fullName", user.getFullName(),
                    "role", user.getRole()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}