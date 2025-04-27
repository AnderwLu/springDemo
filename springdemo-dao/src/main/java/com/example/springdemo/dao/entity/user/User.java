package com.example.springdemo.dao.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 真实姓名
     */
    @Column(name = "real_name", length = 100)
    private String realName;

    /**
     * 年龄
     */
    @Column
    private Integer age;

    /**
     * 性别
     */
    @Column(length = 10)
    private String gender;

    /**
     * 地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 角色
     */
    @Column(length = 50)
    private String role;

    /**
     * 是否启用
     */
    @Column
    private Boolean enabled;
}