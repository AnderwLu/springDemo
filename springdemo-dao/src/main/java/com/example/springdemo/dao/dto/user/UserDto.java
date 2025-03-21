package com.example.springdemo.dao.dto.user;

import lombok.Data;

@Data
public class UserDto {
  private Long id;

  private String username;

  private String password;

  private String email;

  private String phone;

  private String realName;

  private Integer age;

  private String gender;

  private String address;

  private String role;

  private Boolean enabled = true;
}
