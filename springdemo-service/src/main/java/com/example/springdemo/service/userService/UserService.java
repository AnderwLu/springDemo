package com.example.springdemo.service.userService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.PageRequest;

import com.example.springdemo.dao.dto.user.UserDto;

public interface UserService {
  List<UserDto> findAll();

  List<UserDto> findAll(UserDto userDto, PageRequest pageRequest);

  UserDto createUser(UserDto user);

  void updateUser(Long id, UserDto userDto);

  void deleteUser(Long id);

  void exportUsers(UserDto userDto, HttpServletResponse response) throws Exception;
}
