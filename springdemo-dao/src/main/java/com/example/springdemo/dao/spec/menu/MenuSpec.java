package com.example.springdemo.dao.spec.menu;



import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.springdemo.dao.dto.menu.MenuDto;
import com.example.springdemo.dao.entity.menu.Menu;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;


public class MenuSpec implements Specification<Menu> {

  private MenuDto menuDto;

  public MenuSpec(MenuDto menuDto) {
    this.menuDto = menuDto;
  }

  @Override
  public Predicate toPredicate(@SuppressWarnings("null") Root<Menu> root,
                               @SuppressWarnings("null") CriteriaQuery<?> query, @SuppressWarnings("null") CriteriaBuilder criteriaBuilder) {
    
    List<Predicate> predicates = new ArrayList<>();
    
    if (menuDto == null) {
      return criteriaBuilder.conjunction();
    }
    
    // 名称模糊查询
    if (StringUtils.hasText(menuDto.getName())) {
      predicates.add(criteriaBuilder.like(root.<String>get("name"), "%" + menuDto.getName() + "%"));
    }
    
    // 按类型查询
    if (StringUtils.hasText(menuDto.getType())) {
      predicates.add(criteriaBuilder.equal(root.get("type"), menuDto.getType()));
    }
    
    // 按父菜单ID查询
    if (menuDto.getParentId() != null) {
      predicates.add(criteriaBuilder.equal(root.get("parentId"), menuDto.getParentId()));
    }
    
    // 按激活状态查询
    if (menuDto.getIsActive() != null) {
      predicates.add(criteriaBuilder.equal(root.get("isActive"), menuDto.getIsActive()));
    }
    
    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
  }
} 