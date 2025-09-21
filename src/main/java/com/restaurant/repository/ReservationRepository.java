package com.restaurant.repository;

import com.restaurant.entity.Reservation;
import com.restaurant.entity.User;
import com.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByRestaurant(Restaurant restaurant);
    List<Reservation> findByReservationDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Reservation> findByUserOrderByCreatedAtDesc(User user);
}