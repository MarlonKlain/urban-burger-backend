package com.example.urban_burger.repository;

import com.example.urban_burger.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByUserId(Long userId);
}
