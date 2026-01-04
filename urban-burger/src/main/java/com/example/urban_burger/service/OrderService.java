package com.example.urban_burger.service;

import com.example.urban_burger.entity.Order;
import com.example.urban_burger.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WhatsAppService whatsAppService;

    public Order createOrder(Order order) {
        order.setStatus("PENDING");
        Order savedOrder = repository.save(order);

        // Broadcast the new order
        messagingTemplate.convertAndSend("/topic/orders", savedOrder);

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        order.setStatus(status);
        Order updatedOrder = repository.save(order);

        // Broadcast the update
        messagingTemplate.convertAndSend("/topic/orders", updatedOrder);

        // Send WhatsApp notification
        whatsAppService.sendStatusUpdate(updatedOrder.getCustomerPhone(), status, id);

        return updatedOrder;
    }
}
