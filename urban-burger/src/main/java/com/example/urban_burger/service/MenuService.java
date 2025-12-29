package com.example.urban_burger.service;

import com.example.urban_burger.entity.MenuItem;
import com.example.urban_burger.entity.User;
import com.example.urban_burger.repository.MenuItemRepository;
import com.example.urban_burger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<MenuItem> getAllMenuItems() {
        User user = getCurrentUser();
        return menuItemRepository.findByUserId(user.getId());
    }

    public MenuItem createMenuItem(MenuItem menuItem) {
        User user = getCurrentUser();
        menuItem.setUser(user);
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(Long id, MenuItem updatedItem) {
        User user = getCurrentUser();
        MenuItem existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (!existingItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        existingItem.setName(updatedItem.getName());
        existingItem.setIngredients(updatedItem.getIngredients());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setImageUrl(updatedItem.getImageUrl());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setFeatured(updatedItem.isFeatured());

        return menuItemRepository.save(existingItem);
    }

    public void deleteMenuItem(Long id) {
        User user = getCurrentUser();
        MenuItem existingItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        if (!existingItem.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        menuItemRepository.delete(existingItem);
    }
}
