package com.example.springdemo.batch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 批处理控制器
 */
@RestController
@RequestMapping("/api/batch")
public class BatchController {

    private static final Logger log = LoggerFactory.getLogger(BatchController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importUserJob;

    /**
     * 上传CSV文件并导入用户数据
     */
    @PostMapping("/import-users")
    public ResponseEntity<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存上传的文件
            String filename = "users.csv";
            Path path = Paths.get("src/main/resources/" + filename);
            Files.write(path, file.getBytes());
            
            log.info("文件已保存: {}", path.toAbsolutePath());
            
            // 启动批处理作业
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", new Date().getTime())
                    .toJobParameters();
            
            jobLauncher.run(importUserJob, jobParameters);
            
            response.put("code", 200);
            response.put("message", "文件上传成功，导入作业已启动");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导入用户数据失败", e);
            response.put("code", 500);
            response.put("message", "导入失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
} 