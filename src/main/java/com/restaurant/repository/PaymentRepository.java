package com.restaurant.repository;

import com.restaurant.entity.Payment;
import com.restaurant.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
    List<Payment> findByStatus(com.restaurant.entity.PaymentStatus status);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByMethod(com.restaurant.entity.PaymentMethod method);
}