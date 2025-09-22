// service/impl/PaymentServiceImpl.java - IMPLEMENTACIÓN FALTANTE
package com.restaurant.service.impl;

import com.restaurant.controller.exceptions.ResourceNotFoundException;
import com.restaurant.controller.exceptions.BusinessException;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.entity.*;
import com.restaurant.mappers.PaymentMapper;
import com.restaurant.repository.PaymentRepository;
import com.restaurant.repository.OrderRepository;
import com.restaurant.service.PaymentService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Override
    public PaymentDTO processPayment(Long orderId, PaymentDTO paymentDTO) throws ServiceException {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            // Verificar si ya existe un pago para esta orden
            paymentRepository.findByOrder(order).ifPresent(existingPayment -> {
                // Si ya existe, actualizar en lugar de crear uno nuevo
                existingPayment.setStatus(PaymentStatus.valueOf(paymentDTO.getStatus()));
                existingPayment.setTransactionId(paymentDTO.getTransactionId());
                existingPayment.setPaymentProvider(paymentDTO.getPaymentProvider());
                existingPayment.setProviderPaymentId(paymentDTO.getProviderPaymentId());

                if ("COMPLETED".equals(paymentDTO.getStatus())) {
                    existingPayment.setPaidAt(LocalDateTime.now());
                }

                paymentRepository.save(existingPayment);
            });

            // Si no existe, crear nuevo pago
            if (paymentRepository.findByOrder(order).isEmpty()) {
                Payment payment = new Payment(order, order.getTotalAmount(),
                        PaymentMethod.valueOf(paymentDTO.getMethod()));

                payment.setTransactionId(paymentDTO.getTransactionId() != null ?
                        paymentDTO.getTransactionId() : UUID.randomUUID().toString());
                payment.setStatus(PaymentStatus.valueOf(paymentDTO.getStatus()));
                payment.setPaymentProvider(paymentDTO.getPaymentProvider());
                payment.setProviderPaymentId(paymentDTO.getProviderPaymentId());
                payment.setCardLast4(paymentDTO.getCardLast4());
                payment.setCardBrand(paymentDTO.getCardBrand());

                if ("COMPLETED".equals(paymentDTO.getStatus())) {
                    payment.setPaidAt(LocalDateTime.now());
                }

                Payment savedPayment = paymentRepository.save(payment);
                return paymentMapper.toDTO(savedPayment);
            }

            // Retornar el pago actualizado
            Payment updatedPayment = paymentRepository.findByOrder(order).get();
            return paymentMapper.toDTO(updatedPayment);

        } catch (Exception e) {
            throw new ServiceException("Error al procesar pago", e);
        }
    }

    @Override
    public PaymentDTO getPaymentByOrderId(Long orderId) throws ServiceException {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

            Payment payment = paymentRepository.findByOrder(order)
                    .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

            return paymentMapper.toDTO(payment);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener pago", e);
        }
    }

    @Override
    public PaymentDTO updatePaymentStatus(Long paymentId, String status) throws ServiceException {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

            payment.setStatus(PaymentStatus.valueOf(status));

            if (PaymentStatus.COMPLETED.name().equals(status)) {
                payment.setPaidAt(LocalDateTime.now());
            }

            Payment updatedPayment = paymentRepository.save(payment);
            return paymentMapper.toDTO(updatedPayment);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar estado del pago", e);
        }
    }

    @Override
    public PaymentDTO refundPayment(Long paymentId) throws ServiceException {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));

            if (!PaymentStatus.COMPLETED.equals(payment.getStatus())) {
                throw new BusinessException("Solo se pueden reembolsar pagos completados");
            }

            payment.setStatus(PaymentStatus.REFUNDED);
            Payment refundedPayment = paymentRepository.save(payment);

            return paymentMapper.toDTO(refundedPayment);
        } catch (Exception e) {
            throw new ServiceException("Error al procesar reembolso", e);
        }
    }

    @Override
    public PaymentDTO create(PaymentDTO dto) throws ServiceException {
        try {
            Payment payment = paymentMapper.toEntity(dto);
            Payment savedPayment = paymentRepository.save(payment);
            return paymentMapper.toDTO(savedPayment);
        } catch (Exception e) {
            throw new ServiceException("Error al crear pago", e);
        }
    }

    @Override
    public PaymentDTO update(Long id, PaymentDTO dto) throws ServiceException {
        try {
            if (!paymentRepository.existsById(id)) {
                throw new ResourceNotFoundException("Pago no encontrado");
            }

            Payment payment = paymentMapper.toEntity(dto);
            payment.setId(id);
            Payment updatedPayment = paymentRepository.save(payment);
            return paymentMapper.toDTO(updatedPayment);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar pago", e);
        }
    }

    @Override
    public PaymentDTO findById(Long id) throws ServiceException {
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
            return paymentMapper.toDTO(payment);
        } catch (Exception e) {
            throw new ServiceException("Error al buscar pago", e);
        }
    }

    @Override
    public void deleteById(Long id) throws ServiceException {
        try {
            if (!paymentRepository.existsById(id)) {
                throw new ResourceNotFoundException("Pago no encontrado");
            }
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException("Error al eliminar pago", e);
        }
    }

    @Override
    public List<PaymentDTO> findAll() throws ServiceException {
        try {
            List<Payment> payments = paymentRepository.findAll();
            return paymentMapper.toDTOs(payments);
        } catch (Exception e) {
            throw new ServiceException("Error al obtener pagos", e);
        }
    }
}