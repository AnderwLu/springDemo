package com.example.springdemo.web.controller;

import com.example.springdemo.common.result.Result;
import com.example.springdemo.dao.dto.user.UserDto;
import com.example.springdemo.service.userService.UserService;

import org.springframework.batch.core.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    @Qualifier("exportUserJob")
    private Job exportUserJob;

    /**
     * 获取用户列表（支持模糊搜索）
     */
    @GetMapping
    public Result<List<UserDto>> getUsers(UserDto userDto,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) PageRequest pageRequest) {
        return Result.success(userService.findAll(userDto, pageRequest));
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
    @PostMapping("/import")
    public ResponseEntity<Map<String, Object>> importUsers(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            // TODO: 实现Excel文件解析和数据导入逻辑
            response.put("code", 200);
            response.put("message", "导入用户成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "导入用户失败: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 导出用户数据（使用Spring Batch的ItemStreamWriter进行流式导出）
     * 
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @GetMapping("/export")
    public Result exportUsers(UserDto userDto, HttpServletResponse response) throws Exception {

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 构建文件名
        String filename = "用户表users" + ".xlsx";
        // 防止中文乱码
        String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + encodedFilename);
        userService.exportUsers(userDto, response);
        return Result.success();
    }

}