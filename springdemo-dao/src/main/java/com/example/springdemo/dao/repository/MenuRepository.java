package com.example.springdemo.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.springdemo.dao.entity.menu.Menu;

/**
 * 菜单数据访问接口
 */
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

    /**
     * 查找所有顶级菜单
     */
    List<Menu> findByParentIdIsNullOrderBySortAsc();

    /**
     * 根据父ID查找菜单
     */
    List<Menu> findByParentIdOrderBySortAsc(Long parentId);

    /**
     * 查找所有激活状态的菜单
     */
    List<Menu> findByIsActiveTrueOrderBySortAsc();

    /**
     * 根据类型查找激活状态的菜单
     */
    List<Menu> findByTypeAndIsActiveTrueOrderBySortAsc(String type);
} 