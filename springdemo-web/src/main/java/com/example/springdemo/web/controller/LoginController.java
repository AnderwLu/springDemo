package com.example.springdemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.springdemo.common.result.Result;

/**
 * 登录控制器
 */
@Controller
public class LoginController {

    /**
     * 登录页面
     */
    @GetMapping("/login")
    @ResponseBody
    public Result<String> login() {
        return Result.success("forward:/login.html");
    }
} 