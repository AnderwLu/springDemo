package com.example.springdemo.service.userService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;

import com.example.springdemo.common.result.TableResultResponse;
import com.example.springdemo.dao.dto.user.UserDto;

public interface UserService {

  List<UserDto> findAll(UserDto userDto, Pageable pageable);

  UserDto createUser(UserDto user);

  void updateUser(Long id, UserDto userDto);

  void deleteUser(Long id);

  void exportUsers(UserDto userDto, HttpServletResponse response) throws Exception;

  TableResultResponse<UserDto> findPageable(UserDto userDto, Pageable pageable);
}
