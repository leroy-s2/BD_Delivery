package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.dto.ReservationDTO;
import com.restaurant.entity.Reservation;
import com.restaurant.entity.ReservationStatus;
import com.restaurant.entity.Restaurant;
import com.restaurant.entity.User;
import com.restaurant.mappers.ReservationMapper;
import com.restaurant.repository.ReservationRepository;
import com.restaurant.repository.RestaurantRepository;
import com.restaurant.repository.UserRepository;
import com.restaurant.service.ReservationService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public ReservationDTO create(ReservationDTO dto) throws ServiceException {
        try {
            Reservation reservation = reservationMapper.toEntity(dto);

            // Set user and restaurant
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            reservation.setUser(user);
            reservation.setRestaurant(restaurant);
            reservation.setStatus(ReservationStatus.PENDING);
            reservation.setCreatedAt(LocalDateTime.now());

            Reservation savedReservation = reservationRepository.save(reservation);
            return reservationMapper.toDTO(savedReservation);
        } catch (Exception e) {
            throw new ServiceException("Error al crear reserva", e);
        }
    }

    @Override
    public ReservationDTO update(Long id, ReservationDTO dto) throws ServiceException {
        try {
            Reservation existingReservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

            existingReservation.setReservationDateTime(dto.getReservationDateTime());
            existingReservation.setPartySize(dto.getPartySize());
            existingReservation.setSpecialRequests(dto.getSpecialRequests());

            Reservation updatedReservation = reservationRepository.save(existingReservation);
            return reservationMapper.toDTO(updatedReservation);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar reserva", e);
        }
    }

    @Override
    public ReservationDTO findById(Long id) throws ServiceException {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));
            return reservationMapper.toDTO(reservation);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar reserva", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!reservationRepository.existsById(id)) {
                throw new ResourceNotFoundException("Reserva no encontrada");
            }
            reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar reserva", e);
        }
    }

    @Override
    public List<ReservationDTO> findAll() throws ServiceException {
        try {
            List<Reservation> reservations = reservationRepository.findAll();
            return reservationMapper.toDTOs(reservations);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener reservas", e);
        }
    }

    @Override
    public List<ReservationDTO> findByUserId(Long userId) throws ServiceException {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            List<Reservation> reservations = reservationRepository.findByUserOrderByCreatedAtDesc(user);
            return reservationMapper.toDTOs(reservations);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener reservas del usuario", e);
        }
    }

    @Override
    public List<ReservationDTO> findByRestaurantId(Long restaurantId) throws ServiceException {
        try {
            Restaurant restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado"));

            List<Reservation> reservations = reservationRepository.findByRestaurant(restaurant);
            return reservationMapper.toDTOs(reservations);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener reservas del restaurante", e);
        }
    }

    @Override
    public List<ReservationDTO> findByDateRange(LocalDateTime start, LocalDateTime end) throws ServiceException {
        try {
            List<Reservation> reservations = reservationRepository.findByReservationDateTimeBetween(start, end);
            return reservationMapper.toDTOs(reservations);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar reservas por fecha", e);
        }
    }

    @Override
    public ReservationDTO updateStatus(Long reservationId, String status) throws ServiceException {
        try {
            Reservation reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

            reservation.setStatus(ReservationStatus.valueOf(status));
            Reservation updatedReservation = reservationRepository.save(reservation);
            return reservationMapper.toDTO(updatedReservation);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar estado de reserva", e);
        }
    }
}