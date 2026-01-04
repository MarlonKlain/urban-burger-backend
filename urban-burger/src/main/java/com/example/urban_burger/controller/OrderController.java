package com.example.urban_burger.controller;

import com.example.urban_burger.entity.Order;
import com.example.urban_burger.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/public/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(service.createOrder(order));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        System.out.println("Received status update request for Order ID: " + id + " with status: " + status);
        // Simple string body often comes with quotes if raw json, but let's assume raw
        // text or we might need to trim quotes if sent as JSON string
        String cleanStatus = status.replace("\"", "");
        System.out.println("Clean status: " + cleanStatus);
        return ResponseEntity.ok(service.updateOrderStatus(id, cleanStatus));
    }
}
