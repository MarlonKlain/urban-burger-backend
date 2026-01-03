package com.example.urban_burger.service;

import com.example.urban_burger.entity.Order;
import com.example.urban_burger.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final SimpMessagingTemplate messagingTemplate;

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
}
