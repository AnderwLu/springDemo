package com.example.springdemo.web.controller;

import com.example.springdemo.dao.entity.User;
import com.example.springdemo.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取用户列表（支持模糊搜索）
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users;
            
            if (username != null || email != null) {
                // 创建查询条件
                User probe = new User();
                probe.setUsername(username);
                probe.setEmail(email);
                
                // 创建匹配器，设置模糊匹配
                ExampleMatcher matcher = ExampleMatcher.matching()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                        .withIgnoreCase()
                        .withIgnoreNullValues();
                
                // 执行查询
                users = userRepository.findAll(Example.of(probe, matcher));
            } else {
                users = userRepository.findAll();
            }
            
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

    /**
     * 新增用户
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查用户名是否已存在
            if (userRepository.existsByUsername(user.getUsername())) {
                response.put("code", 400);
                response.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 保存用户
            User savedUser = userRepository.save(user);
            savedUser.setPassword("[PROTECTED]");
            
            response.put("code", 200);
            response.put("message", "创建用户成功");
            response.put("data", savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "创建用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查用户是否存在
            Optional<User> existingUser = userRepository.findById(id);
            if (!existingUser.isPresent()) {
                response.put("code", 404);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
            
            // 检查用户名是否与其他用户重复
            User userWithSameUsername = userRepository.findByUsername(user.getUsername());
            if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
                response.put("code", 400);
                response.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 更新用户信息
            User userToUpdate = existingUser.get();
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                userToUpdate.setPassword(user.getPassword());
            }
            
            User updatedUser = userRepository.save(userToUpdate);
            updatedUser.setPassword("[PROTECTED]");
            
            response.put("code", 200);
            response.put("message", "更新用户成功");
            response.put("data", updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "更新用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查用户是否存在
            if (!userRepository.existsById(id)) {
                response.put("code", 404);
                response.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
            
            userRepository.deleteById(id);
            
            response.put("code", 200);
            response.put("message", "删除用户成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "删除用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 导入用户数据
     */
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            // TODO: 实现Excel文件解析和数据导入逻辑
            response.put("code", 200);
            response.put("message", "导入用户成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "导入用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 导出用户数据
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportUsers() {
        try {
            // TODO: 实现数据导出到Excel的逻辑
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/vnd.ms-excel")
                    .header("Content-Disposition", "attachment; filename=users.xlsx")
                    .body(new byte[0]); // 临时返回空数据
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 