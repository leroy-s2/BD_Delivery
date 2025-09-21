package com.restaurant.controller;

import com.restaurant.dto.ReservationDTO;
import com.restaurant.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUser(@PathVariable Long userId) {
        try {
            List<ReservationDTO> reservations = reservationService.findByUserId(userId);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        try {
            ReservationDTO reservation = reservationService.findById(id);
            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> reservationData) {
        try {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setUserId(Long.valueOf(reservationData.get("userId").toString()));
            reservationDTO.setRestaurantId(Long.valueOf(reservationData.get("restaurantId").toString()));
            reservationDTO.setReservationDateTime(LocalDateTime.parse(reservationData.get("reservationDateTime").toString()));
            reservationDTO.setPartySize(Integer.valueOf(reservationData.get("partySize").toString()));
            reservationDTO.setSpecialRequests(reservationData.get("specialRequests").toString());

            ReservationDTO savedReservation = reservationService.create(reservationDTO);
            return ResponseEntity.ok(savedReservation);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear la reserva: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable Long id, @RequestBody ReservationDTO reservationDTO) {
        try {
            ReservationDTO updatedReservation = reservationService.update(id, reservationDTO);
            return ResponseEntity.ok(updatedReservation);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        try {
            reservationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}