package com.example.springdemo.dao.entity.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelProperty("用户ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @ExcelProperty("用户名")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    @ExcelProperty("电子邮箱")
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "real_name", length = 50)
    private String realName;

    private Integer age;

    @Column(length = 10)
    private String gender;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, length = 50)
    private String role;

    @Column(nullable = false)
    private Boolean enabled = true;
}