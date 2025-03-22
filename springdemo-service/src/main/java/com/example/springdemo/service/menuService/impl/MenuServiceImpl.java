package com.example.springdemo.service.menuService.impl;

import com.example.springdemo.dao.dto.menu.MenuDto;
import com.example.springdemo.dao.entity.menu.Menu;
import com.example.springdemo.dao.repository.MenuRepository;
import com.example.springdemo.dao.spec.menu.MenuSpec;
import com.example.springdemo.service.menuService.MenuService;

import cn.hutool.core.bean.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MenuServiceImpl implements MenuService {
  
  @Autowired
  private MenuRepository menuRepository;

  @Override
  public List<MenuDto> findAll() {
    List<Menu> menus = menuRepository.findAll();
    return BeanUtil.copyToList(menus, MenuDto.class);
  }

  @Override
  public List<MenuDto> findAll(MenuDto menuDto, Pageable pageable) {
    Page<Menu> menus = menuRepository.findAll(new MenuSpec(menuDto), pageable);
    return BeanUtil.copyToList(menus.getContent(), MenuDto.class);
  }

  @Override
  public List<MenuDto> findTreeMenu() {
    // 获取所有根菜单
    List<Menu> rootMenus = menuRepository.findByParentIdIsNullOrderBySortAsc();
    List<MenuDto> result = new ArrayList<>();
    
    // 递归构建菜单树
    for (Menu rootMenu : rootMenus) {
      MenuDto rootDto = BeanUtil.copyProperties(rootMenu, MenuDto.class);
      buildMenuTree(rootDto);
      result.add(rootDto);
    }
    
    return result;
  }
  
  /**
   * 递归构建子菜单
   */
  private void buildMenuTree(MenuDto parentDto) {
    List<Menu> children = menuRepository.findByParentIdOrderBySortAsc(parentDto.getId());
    if (children != null && !children.isEmpty()) {
      List<MenuDto> childrenDto = BeanUtil.copyToList(children, MenuDto.class);
      parentDto.setChildren(childrenDto);
      
      // 递归处理每个子菜单
      for (MenuDto child : childrenDto) {
        buildMenuTree(child);
      }
    }
  }

  @Override
  public List<MenuDto> findByType(String type) {
    List<Menu> menus = menuRepository.findByTypeAndIsActiveTrueOrderBySortAsc(type);
    return BeanUtil.copyToList(menus, MenuDto.class);
  }

  @Override
  public MenuDto createMenu(MenuDto menuDto) {
    Menu menuEntity = BeanUtil.copyProperties(menuDto, Menu.class);
    menuRepository.save(menuEntity);
    return BeanUtil.copyProperties(menuEntity, MenuDto.class);
  }

  @Override
  public void updateMenu(Long id, MenuDto menuDto) {
    Menu menuEntity = menuRepository.findById(id).orElseThrow(() -> new RuntimeException("菜单不存在"));
    BeanUtil.copyProperties(menuDto, menuEntity);
    menuRepository.save(menuEntity);
  }

  @Override
  public void deleteMenu(Long id) {
    // 检查是否有子菜单
    List<Menu> children = menuRepository.findByParentIdOrderBySortAsc(id);
    if (children != null && !children.isEmpty()) {
      throw new RuntimeException("该菜单包含子菜单，无法删除");
    }
    menuRepository.deleteById(id);
  }

  @Override
  public Map<String, Object> getAdminPageData(String pageKey) {
    Map<String, Object> result = new HashMap<>();
    
    // 获取所有可用的管理菜单
    List<MenuDto> adminMenus = findByType("admin");
    
    // 标记当前活动菜单
    for (MenuDto menu : adminMenus) {
      if (menu.getUrl() != null && menu.getUrl().contains(pageKey)) {
        menu.setActive(true);
        result.put("active", menu.getName().toLowerCase());
      }
      
      // 处理子菜单
      for (MenuDto child : menu.getChildren()) {
        if (child.getUrl() != null && child.getUrl().contains(pageKey)) {
          child.setActive(true);
          menu.setActive(true);
          result.put("active", child.getName().toLowerCase());
        }
      }
    }
    
    // 添加系统信息
    result.put("osName", System.getProperty("os.name"));
    result.put("javaVersion", System.getProperty("java.version"));
    result.put("serverTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    
    // 创建面包屑导航
    List<Map<String, Object>> breadcrumbs = new ArrayList<>();
    createBreadcrumbs(breadcrumbs, adminMenus, pageKey);
    
    result.put("breadcrumbs", breadcrumbs);
    result.put("title", getTitleByPageKey(pageKey));
    
    return result;
  }
  
  /**
   * 创建面包屑导航
   */
  private void createBreadcrumbs(List<Map<String, Object>> breadcrumbs, List<MenuDto> menus, String pageKey) {
    // 添加首页
    Map<String, Object> home = new HashMap<>();
    home.put("name", "首页");
    home.put("url", "/admin/dashboard");
    home.put("active", "dashboard".equals(pageKey));
    breadcrumbs.add(home);
    
    // 添加当前页面的层级路径
    for (MenuDto menu : menus) {
      if (menu.getUrl() != null && menu.getUrl().contains(pageKey)) {
        Map<String, Object> current = new HashMap<>();
        current.put("name", menu.getName());
        current.put("active", true);
        breadcrumbs.add(current);
        return;
      }
      
      // 检查子菜单
      for (MenuDto child : menu.getChildren()) {
        if (child.getUrl() != null && child.getUrl().contains(pageKey)) {
          // 添加父级
          Map<String, Object> parent = new HashMap<>();
          parent.put("name", menu.getName());
          parent.put("url", menu.getUrl());
          parent.put("active", false);
          breadcrumbs.add(parent);
          
          // 添加当前
          Map<String, Object> current = new HashMap<>();
          current.put("name", child.getName());
          current.put("active", true);
          breadcrumbs.add(current);
          return;
        }
      }
    }
  }
  
  /**
   * 根据页面键获取页面标题
   */
  private String getTitleByPageKey(String pageKey) {
    switch (pageKey) {
      case "dashboard":
        return "仪表盘";
      case "index":
        return "管理系统首页";
      case "users":
        return "用户管理";
      case "batch":
        return "批处理管理";
      case "batch/import":
        return "批量导入";
      case "monitor":
        return "监控模块";
      case "settings":
        return "系统设置";
      default:
        return "管理系统";
    }
  }
} 