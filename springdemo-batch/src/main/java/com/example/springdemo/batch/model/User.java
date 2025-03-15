package com.example.springdemo.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类，用于文件导入
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private Integer age;
    private String gender;
    private String address;
    private String role;
    private Boolean enabled;
    
    // 用于CSV文件导入的构造函数
    public User(String username, String password, String email, String phone, 
                String realName, Integer age, String gender, String address, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.realName = realName;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.enabled = true;
    }
} 