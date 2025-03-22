package com.example.springdemo.dao.entity.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实体类
 */
@Entity
@Table(name = "menus")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 100)
    private String url;

    @Column(length = 50)
    private String icon;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    private Integer sort = 0;

    @Column(length = 50)
    private String permission;

    @Column(length = 50)
    private String type;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Transient
    private List<Menu> children = new ArrayList<>();
} 