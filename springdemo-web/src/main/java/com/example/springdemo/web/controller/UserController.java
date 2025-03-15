package com.example.springdemo.web.controller;

import com.example.springdemo.dao.entity.User;
import com.example.springdemo.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有用户列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userRepository.findAll();
            
            // 处理密码，不返回给前端
            users.forEach(user -> user.setPassword("[PROTECTED]"));
            
            response.put("code", 200);
            response.put("message", "获取用户列表成功");
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取用户列表失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 