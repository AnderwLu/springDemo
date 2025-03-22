package com.example.springdemo.dao.dto.menu;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MenuDto {
    private Long id;
    
    private String name;
    
    private String url;
    
    private String icon;
    
    private Long parentId;
    
    private Integer sort = 0;
    
    private String permission;
    
    private String type;
    
    private Boolean isActive = true;
    
    private List<MenuDto> children = new ArrayList<>();
    
    // 额外字段，用于UI显示
    private Boolean active = false;
} 