package com.example.springdemo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring Boot应用程序入口类
 */
@SpringBootApplication(scanBasePackages = {
    "com.example.springdemo.web",
    "com.example.springdemo.service",
    "com.example.springdemo.dao",
    "com.example.springdemo.common",
    "com.example.springdemo.batch"
})
@ComponentScan(basePackages = "com.example.springdemo")
@EntityScan(basePackages = "com.example.springdemo.dao.entity")
@EnableJpaRepositories(basePackages = "com.example.springdemo.dao.repository")
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }
} 