package com.example.urban_burger.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WhatsAppService {

    @Value("${waha.base-url}")
    private String wahaBaseUrl;

    @Value("${waha.session}")
    private String wahaSession;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendStatusUpdate(String phoneNumber, String status, Long orderId) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            System.out.println("No phone number provided for order " + orderId);
            return;
        }

        String chatId = formatPhoneNumber(phoneNumber);
        String message = getMessageForStatus(status, orderId);

        try {
            String url = wahaBaseUrl + "/api/sendText";

            Map<String, Object> body = new HashMap<>();
            body.put("session", wahaSession);
            body.put("chatId", chatId);
            body.put("text", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForObject(url, request, String.class);
            System.out.println("WhatsApp message sent to " + chatId + " for order " + orderId);
        } catch (Exception e) {
            System.err.println("Failed to send WhatsApp message: " + e.getMessage());
            // Do not fail the transaction just because notification failed
        }
    }

    private String formatPhoneNumber(String phone) {
        // 1. Remove non-digits
        String cleaned = phone.replaceAll("\\D", "");

        // 2. Ensure it starts with 55 (Brazil)
        // If length is less than or equal to 11 (e.g. 10 or 11 digits), assume missing
        // country code
        if (cleaned.length() <= 11) {
            cleaned = "55" + cleaned;
        }

        // 3. Handle the "9th digit" rule for Brazil
        // Format: 55 + AA + 9 + XXXXXXXX (Total 13 digits)
        // WAHA/WhatsApp legacy IDs often exclude the 9 after the area code.
        // If we have 13 digits and it's a mobile (starts with 55 + area + 9), remove
        // the 9.
        if (cleaned.length() == 13 && cleaned.startsWith("55") && cleaned.charAt(4) == '9') {
            // Remove the character at index 4 (the '9' after the 2-digit area code)
            // 55 (0,1) AA (2,3) 9 (4) ...
            cleaned = cleaned.substring(0, 4) + cleaned.substring(5);
        }

        return cleaned + "@c.us";
    }

    private String getMessageForStatus(String status, Long orderId) {
        String base = "*Urban Burger*: Atualização do Pedido #" + orderId + "\n\n";
        switch (status) {
            case "PENDING":
                return base + "Recebemos seu pedido! Ele está aguardando confirmação.";
            case "PREPARING":
                return base + "🍔 Seu pedido está sendo preparado com muito carinho!";
            case "READY":
                return base + "✅ Tudo pronto! Seu pedido está aguardando coleta/entregador.";
            case "IN_DELIVERY":
                return base + "🛵 Saiu para entrega! Em breve estará com você.";
            case "COMPLETED":
                return base + "🎉 Entregue! Bom apetite. Esperamos que goste!";
            case "CANCELED":
                return base + "❌ Seu pedido foi cancelado. Entre em contato para mais detalhes.";
            default:
                return base + "Status atualizado para: " + status;
        }
    }
}
