package com.example.springdemo.service.menuService;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.example.springdemo.dao.dto.menu.MenuDto;

public interface MenuService {
  
  /**
   * 获取所有菜单
   */
  List<MenuDto> findAll();

  /**
   * 根据条件分页查询菜单
   */
  List<MenuDto> findAll(MenuDto menuDto, Pageable pageable);

  /**
   * 获取树形菜单
   */
  List<MenuDto> findTreeMenu();

  /**
   * 获取指定类型的菜单
   */
  List<MenuDto> findByType(String type);

  /**
   * 创建菜单
   */
  MenuDto createMenu(MenuDto menuDto);

  /**
   * 更新菜单
   */
  void updateMenu(Long id, MenuDto menuDto);

  /**
   * 删除菜单
   */
  void deleteMenu(Long id);
  
  /**
   * 获取后台页面数据
   */
  Map<String, Object> getAdminPageData(String pageKey);
} 