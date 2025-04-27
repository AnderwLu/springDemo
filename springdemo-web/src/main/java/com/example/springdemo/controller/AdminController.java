package com.example.springdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.common.result.Result;
import com.example.springdemo.service.menuService.MenuService;

import java.util.Map;

/**
 * 管理后台控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private MenuService menuService;

    /**
     * 管理后台首页数据
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        return Result.success(menuService.getAdminPageData("dashboard"));
    }
    
    /**
     * 管理后台主页数据
     */
    @GetMapping("/index")
    public Result<Map<String, Object>> index() {
        return Result.success(menuService.getAdminPageData("index"));
    }

    /**
     * 用户管理页面数据
     */
    @GetMapping("/users")
    public Result<Map<String, Object>> users() {
        return Result.success(menuService.getAdminPageData("users"));
    }

    /**
     * 批处理管理页面数据
     */
    @GetMapping("/batch")
    public Result<Map<String, Object>> batch() {
        return Result.success(menuService.getAdminPageData("batch"));
    }
    
    /**
     * 批量导入页面数据
     */
    @GetMapping("/batch/import")
    public Result<Map<String, Object>> batchImport() {
        return Result.success(menuService.getAdminPageData("batch/import"));
    }

    /**
     * 监控模块页面数据
     */
    @GetMapping("/monitor")
    public Result<Map<String, Object>> monitor() {
        return Result.success(menuService.getAdminPageData("monitor"));
    }

    /**
     * 系统设置页面数据
     */
    @GetMapping("/settings")
    public Result<Map<String, Object>> settings() {
        return Result.success(menuService.getAdminPageData("settings"));
    }
} 