package com.restaurant.service;

import com.restaurant.dto.ReservationDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService extends GenericService<com.restaurant.entity.Reservation, ReservationDTO, Long> {
    List<ReservationDTO> findByUserId(Long userId) throws ServiceException;
    List<ReservationDTO> findByRestaurantId(Long restaurantId) throws ServiceException;
    List<ReservationDTO> findByDateRange(LocalDateTime start, LocalDateTime end) throws ServiceException;
    ReservationDTO updateStatus(Long reservationId, String status) throws ServiceException;
}