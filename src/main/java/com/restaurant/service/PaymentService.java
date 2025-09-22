package com.restaurant.service;

import com.restaurant.dto.PaymentDTO;
import com.restaurant.service.base.GenericService;
import org.hibernate.service.spi.ServiceException;

public interface PaymentService extends GenericService<com.restaurant.entity.Payment, PaymentDTO, Long> {
    PaymentDTO processPayment(Long orderId, PaymentDTO paymentDTO) throws ServiceException;
    PaymentDTO getPaymentByOrderId(Long orderId) throws ServiceException;
    PaymentDTO updatePaymentStatus(Long paymentId, String status) throws ServiceException;
    PaymentDTO refundPayment(Long paymentId) throws ServiceException;
}