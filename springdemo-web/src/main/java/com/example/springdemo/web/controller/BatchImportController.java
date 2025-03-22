package com.example.springdemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.springdemo.common.result.Result;

/**
 * 批量导入控制器
 */
@Controller
@RequestMapping("/batch")
public class BatchImportController {



    /**
     * 显示批量导入页面
     */
    @GetMapping("/import")
    @ResponseBody
    public Result<String> showImportPage() {
        return Result.success("batch/import");
    }
} 