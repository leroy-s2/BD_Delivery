package com.restaurant.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfiguration {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.public-key}")
    private String publicKey;

    @Value("${mercadopago.environment:sandbox}")
    private String environment;

    @PostConstruct
    public void init() {
        // Configurar el Access Token
        MercadoPagoConfig.setAccessToken(accessToken);

        // Log para verificar configuración
        System.out.println("🔧 MercadoPago configurado:");
        System.out.println("   Environment: " + environment);
        System.out.println("   Access Token: " + accessToken.substring(0, 20) + "...");
        System.out.println("   Public Key: " + publicKey.substring(0, 20) + "...");
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isSandbox() {
        return "sandbox".equals(environment);
    }
}