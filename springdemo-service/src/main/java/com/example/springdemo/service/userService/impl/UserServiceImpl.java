package com.example.springdemo.service.userService.impl;

import com.example.springdemo.batch.processor.ReaderUtils;
import com.example.springdemo.batch.writer.EasyExcelItemStreamWriter;
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
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ReaderUtils readerUtils;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  @Autowired
  private JobLauncher jobLauncher;

  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();
    if (users.isEmpty()) {
      throw new RuntimeException("没有用户");
    }
    return BeanUtil.copyToList(users, UserDto.class);
  }

  @Override
  public List<UserDto> findAll(UserDto userDto, PageRequest pageRequest) {
    Page<User> users = userRepository.findAll(new UserSpec(userDto), pageRequest);
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
    EasyExcelItemStreamWriter<User> writer = new EasyExcelItemStreamWriter<>(response, User.class, "用户数据");

    // 创建一个简单的敏感数据处理器
    ItemProcessor<User, User> processor = user -> {
      user.setPassword("[PROTECTED]");
      return user;
    };
    // 创建批处理步骤
    Step exportStep = stepBuilderFactory.get("exportUsersStep")
        .<User, User>chunk(100)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();

    // 创建并执行作业
    Job exportJob = jobBuilderFactory.get("exportUsersJob-" + System.currentTimeMillis())
        .start(exportStep)
        .build();

    // 使用JobLauncher执行作业
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    jobLauncher.run(exportJob, jobParameters);

  }

}
