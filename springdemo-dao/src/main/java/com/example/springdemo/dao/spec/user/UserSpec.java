package com.example.springdemo.dao.spec.user;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.example.springdemo.dao.dto.user.UserDto;
import com.example.springdemo.dao.entity.user.User;

public class UserSpec implements Specification<User> {

  private UserDto userDto;

  public UserSpec(UserDto userDto) {
    this.userDto = userDto;
  }

  @Override
  public Predicate toPredicate(@SuppressWarnings("null") Root<User> root,
      @SuppressWarnings("null") CriteriaQuery<?> query, @SuppressWarnings("null") CriteriaBuilder criteriaBuilder) {
    if (userDto.getUsername() != null) {
      return criteriaBuilder.like(root.get("username"), "%" + userDto.getUsername() + "%");
    }
    return criteriaBuilder.conjunction();
  }
}
