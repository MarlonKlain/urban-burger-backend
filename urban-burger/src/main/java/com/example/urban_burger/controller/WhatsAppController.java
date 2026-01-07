package com.example.urban_burger.controller;

import com.example.urban_burger.service.WhatsAppService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService whatsAppService;

    public WhatsAppController(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @PostMapping("/webhook")
    public void handleWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Received Payload: " + payload);
        whatsAppService.processMessage(payload);
    }
}
