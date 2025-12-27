package com.example.urban_burger.controller;

import com.example.urban_burger.entity.MenuItem;
import com.example.urban_burger.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/menu")
@RequiredArgsConstructor
public class PublicMenuController {

    private final MenuItemRepository menuItemRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<List<MenuItem>> getMenuByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(menuItemRepository.findByUserId(userId));
    }
}
