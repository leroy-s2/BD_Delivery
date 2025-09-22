package com.restaurant.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.restaurant.config.MercadoPagoConfiguration;
import com.restaurant.dto.OrderDTO;
import com.restaurant.dto.PaymentDTO;
import com.restaurant.dto.UserDTO;
import com.restaurant.service.MercadoPagoService;
import com.restaurant.service.OrderService;
import com.restaurant.service.PaymentService;
import com.restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/mercadopago")
@CrossOrigin(origins = "*")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MercadoPagoConfiguration mpConfig;

    @PostMapping("/create-preference")
    public ResponseEntity<?> createPreference(@RequestBody Map<String, Object> requestData) {
        try {
            System.out.println("🚀 Creando preferencia MercadoPago...");

            Long orderId = Long.valueOf(requestData.get("orderId").toString());
            Long userId = Long.valueOf(requestData.get("userId").toString());

            System.out.println("   Order ID: " + orderId);
            System.out.println("   User ID: " + userId);

            OrderDTO order = orderService.findById(orderId);
            UserDTO user = userService.findById(userId);

            String preferenceId = mercadoPagoService.createPreference(order, user);

            System.out.println("✅ Preferencia creada: " + preferenceId);

            Map<String, Object> response = new HashMap<>();
            response.put("preferenceId", preferenceId);
            response.put("publicKey", mpConfig.getPublicKey());
            response.put("environment", mpConfig.isSandbox() ? "sandbox" : "production");

            return ResponseEntity.ok(response);

        } catch (MPException | MPApiException e) {
            System.err.println("❌ Error MercadoPago: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creando preferencia de MercadoPago: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        } catch (Exception e) {
            System.err.println("❌ Error interno: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> receiveWebhook(
            @RequestParam(value = "id", required = false) Long paymentId,
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "order_id", required = false) Long orderId,
            @RequestBody(required = false) Map<String, Object> body) {

        try {
            System.out.println("🔔 Webhook recibido:");
            System.out.println("   Topic: " + topic);
            System.out.println("   Payment ID: " + paymentId);
            System.out.println("   Order ID: " + orderId);
            System.out.println("   Body: " + body);

            if ("payment".equals(topic) && paymentId != null) {
                PaymentDTO paymentDTO = mercadoPagoService.processPaymentNotification(paymentId);

                if (orderId != null) {
                    paymentDTO.setOrderId(orderId);

                    // Crear o actualizar el pago en nuestra base de datos
                    PaymentDTO savedPayment = paymentService.processPayment(orderId, paymentDTO);

                    // Si el pago fue aprobado, actualizar el estado de la orden
                    if ("COMPLETED".equals(savedPayment.getStatus())) {
                        orderService.updateStatus(orderId, "CONFIRMED");
                        System.out.println("✅ Orden confirmada: " + orderId);
                    }
                }

                return ResponseEntity.ok("OK");
            }

            return ResponseEntity.ok("OK");

        } catch (Exception e) {
            // Log the error but return OK to MercadoPago
            System.err.println("❌ Error processing MercadoPago webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("OK");
        }
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        try {
            System.out.println("🔍 Obteniendo detalles del pago: " + paymentId);
            PaymentDTO paymentDTO = mercadoPagoService.processPaymentNotification(paymentId);
            return ResponseEntity.ok(paymentDTO);
        } catch (Exception e) {
            System.err.println("❌ Error obteniendo detalles del pago: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error obteniendo detalles del pago: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // Endpoint para verificar configuración
    @GetMapping("/config")
    public ResponseEntity<?> getConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("publicKey", mpConfig.getPublicKey());
        config.put("environment", mpConfig.isSandbox() ? "sandbox" : "production");
        config.put("status", "configured");
        return ResponseEntity.ok(config);
    }
}