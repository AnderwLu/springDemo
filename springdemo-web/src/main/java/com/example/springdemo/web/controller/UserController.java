package com.example.springdemo.web.controller;

import com.example.springdemo.common.result.Result;
import com.example.springdemo.common.result.TableResultResponse;
import com.example.springdemo.dao.dto.user.UserDto;
import com.example.springdemo.service.userService.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.rmi.server.ExportException;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表（支持模糊搜索）
     */
    @GetMapping
    public Result<TableResultResponse<UserDto>> getUsers(UserDto userDto,
            @PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return Result.success(userService.findPageable(userDto, pageable));
    }

    /*
     * 新增用户
     */
    @SuppressWarnings("rawtypes")
    @PostMapping
    public Result createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return Result.success();
    }

    /**
     * 更新用户
     */
    @SuppressWarnings("rawtypes")
    @PutMapping("/{id}")
    public Result updateUser(
            @PathVariable Long id,
            @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 导入用户数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/import")
    public Result importUsers(@RequestParam("file") MultipartFile file) {
            // TODO: 实现Excel文件解析和数据导入逻辑
        return Result.success();
    }

    /**
     * 导出用户数据（使用Spring Batch的ItemStreamWriter进行流式导出）
     * 
     * @throws Exception
     */
    @GetMapping("/export")
    public void exportUsers(UserDto userDto, HttpServletResponse response) throws ExportException {

        // 设置响应头
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 构建文件名
            String filename = "用户表users" + ".xlsx";
            // 防止中文乱码
            String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + encodedFilename);
            userService.exportUsers(userDto, response);
        } catch (Exception e) {
            log.error("导出用户数据失败: {}", e.getMessage(),e);
            throw new ExportException("导出用户数据失败: " + e.getMessage());
        }
    }

}