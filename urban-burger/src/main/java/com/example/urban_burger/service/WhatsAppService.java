package com.example.urban_burger.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    private final GeminiService geminiService;
    private final RestTemplate restTemplate;

    @Value("${waha.api.url:http://localhost:3000}")
    private String wahaApiUrl;

    public WhatsAppService(GeminiService geminiService) {
        this.geminiService = geminiService;
        this.restTemplate = new RestTemplate();
    }

    @SuppressWarnings("unchecked")
    public void processMessage(Map<String, Object> payload) {
        System.out.println("Processing message payload: " + payload);
        Map<String, Object> payloadData = (Map<String, Object>) payload.get("payload");
        if (payloadData == null) {
            System.out.println("Payload data is null");
            return;
        }

        String from = (String) payloadData.get("from");
        String body = (String) payloadData.get("body");
        System.out.println("From: " + from + ", Body: " + body);

        if (from != null && body != null && !from.contains("@g.us")) { // Ignore groups
            System.out.println("Generating AI response for: " + body);
            // Generate AI response
            try {
                String aiResponse = geminiService.generateResponse(body);
                System.out.println("AI Response: " + aiResponse);
                sendReply(from, aiResponse);
            } catch (Exception e) {
                System.err.println("Error generating AI response: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Ignoring message (group or null fields)");
        }
    }

    private void sendReply(String remoteJid, String text) {
        String url = wahaApiUrl + "/api/sendText";
        System.out.println("Sending reply to: " + remoteJid + " with text: " + text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chatId", remoteJid);
        requestBody.put("text", text);
        requestBody.put("session", "default");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
            System.out.println("Reply sent successfully");
        } catch (Exception e) {
            System.err.println("Error sending WhatsApp message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendStatusUpdate(String phone, String status, Long orderId) {
        String message = "Your order #" + orderId + " is now: " + status;
        String chatId = phone + "@s.whatsapp.net";
        sendReply(chatId, message);
    }
}
