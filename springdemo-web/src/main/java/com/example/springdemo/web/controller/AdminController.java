package com.example.springdemo.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理后台控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * 管理后台首页数据
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        Map<String, Object> result = new HashMap<>();
        
        // 添加系统信息
        result.put("active", "dashboard");
        result.put("osName", System.getProperty("os.name"));
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("serverTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", true);
        breadcrumbs.add(home);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "仪表盘");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 管理后台主页数据
     */
    @GetMapping("/index")
    public ResponseEntity<Map<String, Object>> index() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "home");
        
        // 添加系统信息
        result.put("osName", System.getProperty("os.name"));
        result.put("javaVersion", System.getProperty("java.version"));
        result.put("serverTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/index");
        home.put("active", true);
        breadcrumbs.add(home);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "管理系统首页");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 用户管理页面数据
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> users() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "users");
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", false);
        breadcrumbs.add(home);
        
        Map<String, Object> current = new HashMap<>();
        current.put("name", "用户管理");
        current.put("active", true);
        breadcrumbs.add(current);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "用户管理");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 批处理管理页面数据
     */
    @GetMapping("/batch")
    public ResponseEntity<Map<String, Object>> batch() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "batch");
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", false);
        breadcrumbs.add(home);
        
        Map<String, Object> current = new HashMap<>();
        current.put("name", "批处理管理");
        current.put("active", true);
        breadcrumbs.add(current);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "批处理管理");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 批量导入页面数据
     */
    @GetMapping("/batch/import")
    public ResponseEntity<Map<String, Object>> batchImport() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "batch");
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", false);
        breadcrumbs.add(home);
        
        Map<String, Object> batch = new HashMap<>();
        batch.put("name", "批处理管理");
        batch.put("url", "/admin/batch");
        batch.put("active", false);
        breadcrumbs.add(batch);
        
        Map<String, Object> current = new HashMap<>();
        current.put("name", "批量导入");
        current.put("active", true);
        breadcrumbs.add(current);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "批量导入");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 监控模块页面数据
     */
    @GetMapping("/monitor")
    public ResponseEntity<Map<String, Object>> monitor() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "monitor");
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", false);
        breadcrumbs.add(home);
        
        Map<String, Object> current = new HashMap<>();
        current.put("name", "监控模块");
        current.put("active", true);
        breadcrumbs.add(current);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "监控模块");
        
        return ResponseEntity.ok(result);
    }

    /**
     * 系统设置页面数据
     */
    @GetMapping("/settings")
    public ResponseEntity<Map<String, Object>> settings() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("active", "settings");
        
        // 创建面包屑导航
        List<Map<String, Object>> breadcrumbs = new ArrayList<>();
        
        Map<String, Object> home = new HashMap<>();
        home.put("name", "首页");
        home.put("url", "/admin/dashboard");
        home.put("active", false);
        breadcrumbs.add(home);
        
        Map<String, Object> current = new HashMap<>();
        current.put("name", "系统设置");
        current.put("active", true);
        breadcrumbs.add(current);
        
        result.put("breadcrumbs", breadcrumbs);
        result.put("title", "系统设置");
        
        return ResponseEntity.ok(result);
    }
} 