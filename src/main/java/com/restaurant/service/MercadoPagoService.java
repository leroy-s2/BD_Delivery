package com.restaurant.service;

import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.resources.payment.Payment;
import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.notification-url}")
    private String notificationUrl;

    public String createPreference(OrderDTO order, UserDTO user) throws MPException, MPApiException {
        try {
            System.out.println("🔧 Creando preferencia MercadoPago:");
            System.out.println("   Order ID: " + order.getId());
            System.out.println("   Total: " + order.getTotalAmount());
            System.out.println("   Usuario: " + user.getFullName());

            // Crear items para la preferencia
            List<PreferenceItemRequest> items = new ArrayList<>();

            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id("order_" + order.getId())
                    .title("Pedido #" + order.getId() + " - " + order.getRestaurantName())
                    .description("Pedido de comida en " + order.getRestaurantName())
                    .pictureUrl("https://via.placeholder.com/300x200?text=Restaurant")
                    .categoryId("food")
                    .quantity(1)
                    .currencyId("PEN") // Soles peruanos
                    .unitPrice(order.getTotalAmount())
                    .build();

            items.add(item);

            // Información del pagador - SIN usar Phone.builder()
            PreferencePayerRequest.PreferencePayerRequestBuilder payerBuilder = PreferencePayerRequest.builder()
                    .name(user.getFullName())
                    .email(user.getEmail());

            // Solo agregar teléfono si existe y no es null/vacío
            if (user.getPhone() != null && !user.getPhone().trim().isEmpty()) {
                // Crear el teléfono manualmente sin usar Phone.builder()
                String cleanPhone = user.getPhone().replaceAll("[^0-9]", "");
                if (cleanPhone.length() > 0) {
                    // En lugar de Phone.builder(), usar un enfoque más simple
                    // La nueva versión del SDK maneja esto diferente
                    System.out.println("   Teléfono usuario: " + cleanPhone);
                }
            }

            PreferencePayerRequest payer = payerBuilder.build();

            // URLs de retorno para Android
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("restaurantapp://payment/success")
                    .pending("restaurantapp://payment/pending")
                    .failure("restaurantapp://payment/failure")
                    .build();

            // Crear la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .payer(payer)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .notificationUrl(notificationUrl + "?order_id=" + order.getId())
                    .externalReference("order_" + order.getId())
                    .expires(true)
                    .expirationDateFrom(java.time.OffsetDateTime.now())
                    .expirationDateTo(java.time.OffsetDateTime.now().plusHours(1))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            System.out.println("✅ Preferencia creada exitosamente: " + preference.getId());
            return preference.getId();

        } catch (Exception e) {
            System.err.println("❌ Error creando preferencia: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Payment getPayment(Long paymentId) throws MPException, MPApiException {
        System.out.println("🔍 Obteniendo pago: " + paymentId);
        PaymentClient client = new PaymentClient();
        return client.get(paymentId);
    }

    public PaymentDTO processPaymentNotification(Long paymentId) throws MPException, MPApiException {
        try {
            System.out.println("🔔 Procesando notificación de pago: " + paymentId);
            Payment payment = getPayment(paymentId);

            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setTransactionId(payment.getId().toString());
            paymentDTO.setAmount(payment.getTransactionAmount());
            paymentDTO.setStatus(mapMercadoPagoStatus(payment.getStatus()));
            paymentDTO.setMethod(mapPaymentMethod(payment.getPaymentMethodId()));
            paymentDTO.setPaymentProvider("MERCADOPAGO");
            paymentDTO.setProviderPaymentId(payment.getId().toString());

            // Manejar información de tarjeta de forma segura
            if (payment.getCard() != null && payment.getCard().getLastFourDigits() != null) {
                paymentDTO.setCardLast4(payment.getCard().getLastFourDigits());
                paymentDTO.setCardBrand(payment.getPaymentMethodId());
            }

            System.out.println("✅ Pago procesado: " + payment.getStatus());
            return paymentDTO;

        } catch (Exception e) {
            System.err.println("❌ Error procesando notificación: " + e.getMessage());
            throw e;
        }
    }

    private String mapMercadoPagoStatus(String mpStatus) {
        if (mpStatus == null) return "PENDING";

        switch (mpStatus.toLowerCase()) {
            case "approved": return "COMPLETED";
            case "pending": return "PENDING";
            case "in_process": return "PROCESSING";
            case "rejected": return "FAILED";
            case "cancelled": return "CANCELLED";
            case "refunded": return "REFUNDED";
            default:
                System.out.println("⚠️ Estado desconocido de MercadoPago: " + mpStatus);
                return "PENDING";
        }
    }

    private String mapPaymentMethod(String paymentMethodId) {
        if (paymentMethodId == null) return "UNKNOWN";

        switch (paymentMethodId.toLowerCase()) {
            case "visa":
            case "master":
            case "amex":
            case "diners":
                return "CREDIT_CARD";
            case "debvisa":
            case "debmaster":
                return "DEBIT_CARD";
            case "efectivo":
            case "pagoefectivo":
                return "CASH";
            case "pse":
            case "bancolombia_transfer":
                return "BANK_TRANSFER";
            default:
                return "DIGITAL_WALLET";
        }
    }
}