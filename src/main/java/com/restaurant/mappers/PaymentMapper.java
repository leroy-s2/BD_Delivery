package com.restaurant.mappers;

import com.restaurant.dto.PaymentDTO;
import com.restaurant.entity.Payment;
import com.restaurant.mappers.base.BaseMappers;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper implements BaseMappers<Payment, PaymentDTO> {

    @Override
    public PaymentDTO toDTO(Payment entity) {
        if (entity == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder().getId());
        dto.setAmount(entity.getAmount());
        dto.setMethod(entity.getMethod().name());
        dto.setStatus(entity.getStatus().name());
        dto.setTransactionId(entity.getTransactionId());
        dto.setPaymentProvider(entity.getPaymentProvider());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPaidAt(entity.getPaidAt());
        dto.setFailureReason(entity.getFailureReason());
        dto.setCardLast4(entity.getCardLast4());
        dto.setCardBrand(entity.getCardBrand());

        return dto;
    }

    @Override
    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) return null;

        Payment entity = new Payment();
        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setTransactionId(dto.getTransactionId());
        entity.setPaymentProvider(dto.getPaymentProvider());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setPaidAt(dto.getPaidAt());
        entity.setFailureReason(dto.getFailureReason());
        entity.setCardLast4(dto.getCardLast4());
        entity.setCardBrand(dto.getCardBrand());

        return entity;
    }

    @Override
    public List<PaymentDTO> toDTOs(List<Payment> list) {
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Payment> toEntityList(List<PaymentDTO> list) {
        return list.stream().map(this::toEntity).collect(Collectors.toList());
    }
}