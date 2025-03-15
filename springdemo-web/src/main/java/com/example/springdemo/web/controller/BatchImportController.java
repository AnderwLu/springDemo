package com.example.springdemo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 批量导入控制器
 */
@Controller
@RequestMapping("/batch")
public class BatchImportController {

    private static final Logger log = LoggerFactory.getLogger(BatchImportController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("importUserJob")
    private Job importUserJob;

    /**
     * 显示批量导入页面
     */
    @GetMapping("/import")
    public String showImportPage() {
        return "batch/import";
    }

    /**
     * 处理用户数据导入
     */
    @PostMapping("/import-users")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        if (file.isEmpty()) {
            response.put("error", "请选择一个文件上传");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 保存上传的文件
            String filename = "users.csv";
            Path resourcesPath = Paths.get("springdemo-batch/src/main/resources");
            if (!Files.exists(resourcesPath)) {
                Files.createDirectories(resourcesPath);
            }
            
            Path filePath = resourcesPath.resolve(filename);
            Files.write(filePath, file.getBytes());
            
            log.info("文件已保存: {}", filePath.toAbsolutePath());
            
            // 启动批处理作业
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", new Date().getTime())
                    .toJobParameters();
            
            jobLauncher.run(importUserJob, jobParameters);
            
            response.put("success", "文件上传成功，导入作业已启动");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导入用户数据失败", e);
            response.put("error", "导入失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
} 