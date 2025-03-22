package com.example.springdemo.service.userService.impl;

import com.example.springdemo.batch.config.BatchConfig;
import com.example.springdemo.batch.processor.Processors;
import com.example.springdemo.batch.reader.ItemReaders;
import com.example.springdemo.batch.writer.ItemWriters;
import com.example.springdemo.dao.dto.user.UserDto;
import com.example.springdemo.dao.entity.user.User;
import com.example.springdemo.dao.repository.UserRepository;
import com.example.springdemo.dao.spec.user.UserSpec;
import com.example.springdemo.service.userService.UserService;

import cn.hutool.core.bean.BeanUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ItemReaders readerUtils;

  @Autowired
  private ItemWriters writerUtils;
  
  @Autowired
  private BatchConfig batchConfig;

  @Autowired
  private JobLauncher jobLauncher;

  @Autowired
  private Processors processors;

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      throw new RuntimeException("没有用户");
    }
    return BeanUtil.copyToList(users, UserDto.class);
  }

  @Override
  public List<UserDto> findAll(UserDto userDto, Pageable pageable) {
    Page<User> users = userRepository.findAll(new UserSpec(userDto), pageable);
    if (users.isEmpty()) {
      throw new RuntimeException("没有用户");
    }
    return BeanUtil.copyToList(users.getContent(), UserDto.class);
  }

  @Override
  public UserDto createUser(UserDto user) {
    User userEntity = BeanUtil.copyProperties(user, User.class);
    userRepository.save(userEntity);
    return BeanUtil.copyProperties(userEntity, UserDto.class);
  }

  @Override
  public void updateUser(Long id, UserDto userDto) {
    User userEntity = userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
    BeanUtil.copyProperties(userDto, userEntity);
    userRepository.save(userEntity);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public void exportUsers(UserDto userDto, HttpServletResponse response) throws Exception {
    // 创建JpaPagingItemReader
    // // 构建动态查询JPQL
    StringBuilder queryBuilder = new StringBuilder("SELECT u FROM User u WHERE 1=1");
    Map<String, Object> parameterValues = new HashMap<>();

    // 添加过滤条件
    if (userDto != null && StringUtils.isNotBlank(userDto.getUsername())) {
      queryBuilder.append(" AND LOWER(u.username) LIKE LOWER(:username)");
      parameterValues.put("username", "%" + userDto.getUsername() + "%");
    }

    if (userDto != null && StringUtils.isNotBlank(userDto.getEmail())) {
      queryBuilder.append(" AND LOWER(u.email) LIKE LOWER(:email)");
      parameterValues.put("email", "%" + userDto.getEmail() + "%");
    }
    queryBuilder.append(" ORDER BY u.id ASC");
    JpaPagingItemReader<User> reader = readerUtils.createUserExportReader(queryBuilder.toString(), parameterValues,
        "userExportReader");
    // 创建读取器和写入器
    ItemStreamWriter<User> writer = writerUtils.createEasyExcelItemStreamWriter(response, User.class, "用户数据");
    Job exportJob = batchConfig.jpaImportFileJob(reader, processors.getDefaultProcessor(), writer);

    // 使用JobLauncher执行作业
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    jobLauncher.run(exportJob, jobParameters);

  }

}
