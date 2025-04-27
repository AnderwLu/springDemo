package com.example.springdemo.dao.spec.user;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
      return criteriaBuilder.like(root.<String>get("username"), "%" + userDto.getUsername() + "%");
    }
    return criteriaBuilder.conjunction();
  }
}
