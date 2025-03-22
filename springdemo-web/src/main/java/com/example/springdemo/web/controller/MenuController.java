package com.example.springdemo.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springdemo.common.result.Result;
import com.example.springdemo.dao.dto.menu.MenuDto;
import com.example.springdemo.service.menuService.MenuService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/api/menus")
@Slf4j
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表（支持模糊搜索）
     */
    @GetMapping
    public Result<List<MenuDto>> getMenus(MenuDto menuDto,
            @PageableDefault(page = 0, size = 50, sort = "sort", direction = Sort.Direction.ASC) Pageable pageable) {
        return Result.success(menuService.findAll(menuDto, pageable));
    }

    /**
     * 获取菜单树
     */
    @GetMapping("/tree")
    public Result<List<MenuDto>> getMenuTree() {
        return Result.success(menuService.findTreeMenu());
    }

    /**
     * 根据类型获取菜单
     */
    @GetMapping("/type")
    public Result<List<MenuDto>> getMenusByType(@RequestParam String type) {
        return Result.success(menuService.findByType(type));
    }

    /**
     * 新增菜单
     */
    @SuppressWarnings("rawtypes")
    @PostMapping
    public Result createMenu(@RequestBody MenuDto menuDto) {
        menuService.createMenu(menuDto);
        return Result.success();
    }

    /**
     * 更新菜单
     */
    @SuppressWarnings("rawtypes")
    @PutMapping("/{id}")
    public Result updateMenu(
            @PathVariable Long id,
            @RequestBody MenuDto menuDto) {
        menuService.updateMenu(id, menuDto);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Result deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
} 